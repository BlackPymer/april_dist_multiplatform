package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap


expect class GalleryWorker() {
    var wasInitializer: Boolean

    @Composable
    fun Init()

    @Composable
    fun openImageFromGallery(): ImageBitmap?
}