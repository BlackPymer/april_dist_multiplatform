package dev.yarobot.shirmaz.posedetection

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseDetector
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import dev.yarobot.shirmaz.platform.PlatformError
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformLandmark

private class AndroidPoseDetector(override val options: ShirmazPoseDetectorOptions) :
    ShirmazPoseDetector {

    private val poseDetectorOption: PoseDetectorOptions = PoseDetectorOptions.Builder()
        .setDetectorMode(
            when (options) {
                is ShirmazPoseDetectorOptions.STREAM -> PoseDetectorOptions.STREAM_MODE
                is ShirmazPoseDetectorOptions.SINGLE_IMAGE -> PoseDetectorOptions.SINGLE_IMAGE_MODE
                else -> PoseDetectorOptions.STREAM_MODE
            }
        )
        .build()

    private val poseDetector: PoseDetector = PoseDetection.getClient(poseDetectorOption)

    @OptIn(ExperimentalGetImage::class)
    override fun processImage(
        image: PlatformImage,
        onProcess: (List<PlatformLandmark>?, PlatformError?) -> Unit
    ) {
        val inputImage =
            InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
        poseDetector.process(inputImage)
            .addOnSuccessListener { pose ->
                onProcess(pose.allPoseLandmarks, null)
            }
            .addOnFailureListener { error ->
                onProcess(null, error)
            }
            .addOnCompleteListener {
                image.close()
            }
    }
}

actual fun createPoseDetector(options: ShirmazPoseDetectorOptions): ShirmazPoseDetector =
    AndroidPoseDetector(options)