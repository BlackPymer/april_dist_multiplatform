package dev.yarobot.shirmaz.camera.model

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.platform.PlatformImage


expect class ModelView(screenHeight: Float, screenWidth: Float) {
    @Composable
    fun ModelRendererInit(state: CameraScreenState)

    fun updateModelPosition(image: PlatformImage)
}
