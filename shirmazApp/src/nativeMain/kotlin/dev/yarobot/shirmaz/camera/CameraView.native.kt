package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.ui.Modifier
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
actual fun CameraView(
    modifier: Modifier,
    cameraType: CameraType,
    onImageCaptured: (image: PlatformImage) -> Unit,
    modelView: @Composable BoxScope.() -> Unit,
    imageToDisplay: PlatformImage?
) {
}