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

@OptIn(ExperimentalForeignApi::class)
class IOSPoseDetector(override val options: ShirmazPoseDetectorOptions) : ShirmazPoseDetector {
    private val poseDetectorOption = MLKPoseDetectorOptions()
    private val poseDetector: MLKPoseDetector

    init {
        poseDetectorOption.setDetectorMode(
            when (options) {
                is ShirmazPoseDetectorOptions.STREAM -> MLKPoseDetectorModeStream
                is ShirmazPoseDetectorOptions.SINGLE_IMAGE -> MLKPoseDetectorModeSingleImage
                else -> MLKPoseDetectorModeStream
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
        val results = poseDetector.resultsInImage(
            image = image as objcnames.protocols.MLKCompatibleImageProtocol,
            error = errorPtr.ptr
        ) as List<MLKPose>?
        val processedResults = results?.firstOrNull()?.landmarks as List<MLKPoseLandmark>?
        onProcess(processedResults, errorPtr.value)
    }
}

actual fun createPoseDetector(options: ShirmazPoseDetectorOptions): ShirmazPoseDetector =
    IOSPoseDetector(options)