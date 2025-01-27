package dev.yarobot.shirmaz.posedetection

import dev.yarobot.shirmaz.platform.PlatformError
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformLandmark

interface ShirmazPoseDetector{
    val options: ShirmazPoseDetectorOptions
    fun processImage(
        image: PlatformImage,
        onProcess: (List<PlatformLandmark>?, PlatformError?) -> Unit
    )
}

@Suppress("ClassName")
sealed interface ShirmazPoseDetectorOptions{
    data object STREAM : ShirmazPoseDetectorOptions
    data object SINGLE_IMAGE : ShirmazPoseDetectorOptions
}

expect fun createPoseDetector(options: ShirmazPoseDetectorOptions): ShirmazPoseDetector