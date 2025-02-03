package dev.yarobot.shirmaz.posedetection

import dev.yarobot.shirmaz.platform.PlatformError
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformLandmark

actual fun createPoseDetector(options: ShirmazPoseDetectorOptions): ShirmazPoseDetector {
    return object : ShirmazPoseDetector {
        override val options: ShirmazPoseDetectorOptions
            get() = options

        override fun processImage(
            image: PlatformImage,
            onProcess: (List<PlatformLandmark>?, PlatformError?) -> Unit
        ) {

        }
    }
}