package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
expect fun CameraView(
    cameraType: CameraType,
    onImageCaptured: (image: PlatformImage) -> Unit,
    modelView: @Composable () -> Unit
)