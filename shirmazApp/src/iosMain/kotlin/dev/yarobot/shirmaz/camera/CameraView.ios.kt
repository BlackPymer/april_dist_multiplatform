package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import cocoapods.MLKitVision.MLKVisionImage
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession.Companion.discoverySessionWithDeviceTypes
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDeviceInput.Companion.deviceInputWithDevice
import platform.AVFoundation.AVCaptureDevicePositionFront
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDuoCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInUltraWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetMedium
import platform.AVFoundation.AVCaptureVideoDataOutput
import platform.AVFoundation.AVCaptureVideoDataOutputSampleBufferDelegateProtocol
import platform.AVFoundation.AVCaptureVideoPreviewLayer
import platform.AVFoundation.AVLayerVideoGravityResizeAspectFill
import platform.AVFoundation.AVMediaTypeVideo
import platform.CoreGraphics.CGImageRelease
import platform.CoreImage.CIContext
import platform.CoreImage.CIImage
import platform.CoreImage.createCGImage
import platform.CoreMedia.CMSampleBufferGetImageBuffer
import platform.CoreMedia.CMSampleBufferRef
import platform.CoreVideo.kCVPixelBufferPixelFormatTypeKey
import platform.CoreVideo.kCVPixelFormatType_32BGRA
import platform.Foundation.NSNumber
import platform.UIKit.UIImage
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import platform.darwin.dispatch_async
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import kotlin.native.runtime.GC
import kotlin.native.runtime.NativeRuntimeApi

private val deviceTypes = listOf(
    AVCaptureDeviceTypeBuiltInWideAngleCamera,
    AVCaptureDeviceTypeBuiltInDualWideCamera,
    AVCaptureDeviceTypeBuiltInDualCamera,
    AVCaptureDeviceTypeBuiltInUltraWideCamera,
    AVCaptureDeviceTypeBuiltInDuoCamera
)

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun CameraView(
    modifier: Modifier,
    cameraType: CameraType,
    onImageCaptured: (PlatformImage) -> Unit,
    modelView: @Composable (BoxScope.() -> Unit),
) {
    val camera: AVCaptureDevice? = remember {
        discoverySessionWithDeviceTypes(
            deviceTypes = deviceTypes,
            mediaType = AVMediaTypeVideo,
            position = AVCaptureDevicePositionFront,
        ).devices.firstOrNull() as? AVCaptureDevice
    }
    if (camera != null) {
        RealDeviceCamera(camera, onImageCaptured)
    } else {
        Text(
            """
            Camera is not available on simulator.
            Please try to run on a real iOS device.
        """.trimIndent(), color = Color.Black
        )
    }
}

@OptIn(ExperimentalForeignApi::class, NativeRuntimeApi::class)
@Composable
private fun RealDeviceCamera(
    camera: AVCaptureDevice,
    onImageCaptured: (image: PlatformImage) -> Unit
) {
    val captureSession: AVCaptureSession = remember {
        AVCaptureSession().also { captureSession ->
            val captureDeviceInput: AVCaptureDeviceInput =
                deviceInputWithDevice(device = camera, error = null)!!
            captureSession.addInput(captureDeviceInput)
        }
    }
    val cameraDelegate: AVCaptureVideoDataOutputSampleBufferDelegateProtocol =
        remember {
            object : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {
                private val ciContext = CIContext()
                override fun captureOutput(
                    output: AVCaptureOutput,
                    didOutputSampleBuffer: CMSampleBufferRef?,
                    fromConnection: AVCaptureConnection
                ) {
                    if (didOutputSampleBuffer == null) return
                    val cvBuffer = CMSampleBufferGetImageBuffer(didOutputSampleBuffer) ?: return
                    val ciImage = CIImage.imageWithCVPixelBuffer(cvBuffer)
                    val cgImage = ciContext.createCGImage(ciImage, ciImage.extent)
                    val uiImage = UIImage(cGImage = cgImage)
                    CGImageRelease(cgImage)
                    onImageCaptured(MLKVisionImage(uiImage))
                    GC.collect()
                }
            }
        }

    val controller = remember(camera) {
        IosCameraViewController(
            captureSession = captureSession,
            cameraDelegate = cameraDelegate
        )
    }
    UIKitViewController(
        modifier = Modifier.fillMaxSize(),
        factory = { controller },
        onRelease = { controller.onRelease() },
        properties = UIKitInteropProperties(
            isInteractive = true,
            isNativeAccessibilityEnabled = true
        )
    )
}


@OptIn(ExperimentalForeignApi::class)
private class IosCameraViewController(
    private val captureSession: AVCaptureSession,
    private val cameraDelegate: AVCaptureVideoDataOutputSampleBufferDelegateProtocol,
) : UIViewController(null, null) {
    private val queue: dispatch_queue_t = dispatch_queue_create("cameraQueue", null)

    private val previewLayer = AVCaptureVideoPreviewLayer(session = captureSession)
    private val videoDataOutput = AVCaptureVideoDataOutput()

    override fun viewDidLoad() {
        super.viewDidLoad()
        addVideoOutput()
        setupCaptureSession()
        startSession()
    }

    fun onRelease() {
        captureSession.stopRunning()
    }

    private fun setupCaptureSession() {
        captureSession.sessionPreset = AVCaptureSessionPresetMedium
        previewLayer.videoGravity = AVLayerVideoGravityResizeAspectFill
        previewLayer.setFrame(this.view.bounds)
        this.view.layer.addSublayer(previewLayer)
        captureSession.commitConfiguration()
    }

    private fun startSession() {
        if (!captureSession.isRunning()) {
            dispatch_async(queue) {
                captureSession.startRunning()
            }
        }
    }

    private fun addVideoOutput() {
        videoDataOutput.alwaysDiscardsLateVideoFrames = true
        videoDataOutput.setSampleBufferDelegate(cameraDelegate, queue)
        videoDataOutput.videoSettings = mapOf(
            kCVPixelBufferPixelFormatTypeKey to NSNumber(kCVPixelFormatType_32BGRA)
        )
        if (captureSession.canAddOutput(videoDataOutput)) {
            captureSession.addOutput(videoDataOutput)
        }
    }
}



