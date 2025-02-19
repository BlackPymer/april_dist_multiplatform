package dev.yarobot.shirmaz.render

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.platform.PlatformImage


interface ModelView {
    val screenHeight: Float
    val screenWidth: Float

    @Composable
    fun ModelRendererInit(state: CameraScreenState)

    fun updateModelPosition(image: PlatformImage)
}

expect fun createModelView(
    screenHeight: Float,
    screenWidth: Float
): ModelView