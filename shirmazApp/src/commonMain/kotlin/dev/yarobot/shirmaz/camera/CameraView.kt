package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
expect fun CameraView(
    onImageCaptured: (image: PlatformImage) -> Unit,
    modelView: @Composable () -> Unit,
    screenHeight: Float,
    screenWidth: Float
)