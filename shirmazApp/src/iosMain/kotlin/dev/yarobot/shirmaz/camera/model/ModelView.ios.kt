package dev.yarobot.shirmaz.camera.model

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.camera.CameraScreenState
import dev.yarobot.shirmaz.platform.PlatformImage

actual class ModelView actual constructor(actual val screenHeight:Float, actual val screenWidth:Float) {
    @Composable
    actual fun ModelRendererInit(state: CameraScreenState) {
    }

    actual fun updateModelPosition(image: PlatformImage) {
    }
}