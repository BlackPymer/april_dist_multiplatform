package dev.yarobot.shirmaz.posedetection

import cocoapods.MLKitPoseDetection.MLKPoseDetectorOptions
import cocoapods.MLKitPoseDetectionCommon.MLKCommonPoseDetectorOptions
import cocoapods.MLKitPoseDetectionCommon.MLKPose
import cocoapods.MLKitPoseDetectionCommon.MLKPoseDetector
import cocoapods.MLKitPoseDetectionCommon.MLKPoseDetectorModeSingleImage
import cocoapods.MLKitPoseDetectionCommon.MLKPoseDetectorModeStream
import cocoapods.MLKitPoseDetectionCommon.MLKPoseLandmark
import dev.yarobot.shirmaz.platform.PlatformError
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformLandmark
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import platform.Foundation.NSError
import kotlinx.cinterop.*
import platform.darwin.dispatch_queue_create
import platform.darwin.dispatch_queue_t
import platform.darwin.dispatch_sync

@Suppress("CAST_NEVER_SUCCEEDS")
@OptIn(ExperimentalForeignApi::class)
class IOSPoseDetector(override val options: ShirmazPoseDetectorOptions) : ShirmazPoseDetector {
    private val poseDetectorOption = MLKPoseDetectorOptions()
    private val poseDetector: MLKPoseDetector
    private val queue: dispatch_queue_t = dispatch_queue_create("PoseDetectorQueue", null)
    init {
        poseDetectorOption.setDetectorMode(
            when (options) {
                ShirmazPoseDetectorOptions.STREAM -> MLKPoseDetectorModeStream
                ShirmazPoseDetectorOptions.SINGLE_IMAGE -> MLKPoseDetectorModeSingleImage
            }
        )
        poseDetector = MLKPoseDetector
            .poseDetectorWithOptions(poseDetectorOption as MLKCommonPoseDetectorOptions)
    }

    @OptIn(BetaInteropApi::class)
    @Suppress("Unchecked_cast")
    override fun processImage(
        image: PlatformImage,
        onProcess: (List<PlatformLandmark>?, PlatformError?) -> Unit
    ) {
        val errorPtr = nativeHeap.alloc<ObjCObjectVar<NSError?>>()
        errorPtr.value = null
        dispatch_sync(queue){
            val results = poseDetector.resultsInImage(
                image = image as objcnames.protocols.MLKCompatibleImageProtocol,
                error = errorPtr.ptr
            ) as List<MLKPose>?
            val processedResults = results?.firstOrNull()?.landmarks as List<MLKPoseLandmark>?
            onProcess(processedResults, errorPtr.value)
        }
    }
}

actual fun createPoseDetector(options: ShirmazPoseDetectorOptions): ShirmazPoseDetector =
    IOSPoseDetector(options)