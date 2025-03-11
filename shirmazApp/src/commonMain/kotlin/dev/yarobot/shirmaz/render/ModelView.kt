package dev.yarobot.shirmaz.render

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetector

interface ModelView {
    val poseDetector: ShirmazPoseDetector

    @Composable
    fun ModelRendererInit(model: ThreeDModel)

    fun updateModelPosition(image: PlatformImage)
}

expect fun createModelView(
    poseDetector: ShirmazPoseDetector
): ModelView