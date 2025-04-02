package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap



actual class GalleryWorker actual constructor() {
    @Composable
    actual fun openImageFromGallery(): ImageBitmap? {
        TODO("Not yet implemented")
    }

    @Composable
    actual fun Init() {
    }

    actual var wasInitializer: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
}