package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
expect fun CameraView(
    modifier: Modifier = Modifier,
    cameraType: CameraType,
    onImageCaptured: (image: PlatformImage) -> Unit,
    modelView: @Composable () -> Unit,
    screenHeight: Float,
    screenWidth: Float
)