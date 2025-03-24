package dev.yarobot.shirmaz.render

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetector

interface ModelView {
    val poseDetector: ShirmazPoseDetector

    @Composable
    fun ModelRendererInit(model: ThreeDModel, modifier: Modifier)

    fun updateModelPosition(image: PlatformImage)
}

expect fun createModelView(
    poseDetector: ShirmazPoseDetector
): ModelView