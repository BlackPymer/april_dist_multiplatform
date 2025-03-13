package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.platform.ActualContext
import dev.yarobot.shirmaz.platform.CaptureImage
import dev.yarobot.shirmaz.platform.URI

actual fun capturePhoto(
    context: ActualContext,
    imageCapture: CaptureImage,
    onImageCaptured: (URI) -> Unit
) {
}