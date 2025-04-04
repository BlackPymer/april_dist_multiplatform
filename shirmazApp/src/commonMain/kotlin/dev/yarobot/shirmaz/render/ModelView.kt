package dev.yarobot.shirmaz.render

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.yarobot.shirmaz.camera.CameraIntent
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.PlatformInputImage
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetector

interface ModelView {
    val poseDetector: ShirmazPoseDetector

    @Composable
    fun ModelRendererInit(
        state: CameraScreenState,
        modifier: Modifier,
        onIntent: (CameraIntent) -> Unit
    )

    fun updateModelPosition(image: PlatformImage)
    fun updateModelPosition(image: PlatformInputImage)
}

expect fun createModelView(
    poseDetector: ShirmazPoseDetector
): ModelView