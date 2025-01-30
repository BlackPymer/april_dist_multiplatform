package dev.yarobot.shirmaz.posedetection

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import dev.yarobot.shirmaz.platform.PlatformError
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformLandmark

actual fun createPoseDetector(options: ShirmazPoseDetectorOptions): ShirmazPoseDetector {
    return object : ShirmazPoseDetector {
        override val options: ShirmazPoseDetectorOptions
            get() = options


        @OptIn(ExperimentalGetImage::class)
        override fun processImage(
            image: PlatformImage,
            onProcess: (List<PlatformLandmark>?, PlatformError?) -> Unit
        ) {
            val poseDetectorOptions = PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.STREAM_MODE)
                .build()
            val poseDetector = PoseDetection.getClient(poseDetectorOptions)
            val inputImage =
                InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees)
            poseDetector.process(inputImage)
                .addOnSuccessListener { pose ->
                    val results = pose.allPoseLandmarks
                    onProcess(results, null)
                }
                .addOnFailureListener { error ->
                    onProcess(null, error)
                }
                .addOnCompleteListener {
                    image.close()
                }
        }
    }
}