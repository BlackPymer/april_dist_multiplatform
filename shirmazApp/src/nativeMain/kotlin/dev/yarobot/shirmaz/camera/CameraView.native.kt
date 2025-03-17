package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage


@Composable
actual fun CameraView(
    modifier: Modifier,
    cameraType: CameraType,
    onImageCaptured: (image: PlatformImage) -> Unit,
    onPictureTaken: (image: ImageBitmap) -> Unit,
    capturePhotoStarted: Boolean
) {
}