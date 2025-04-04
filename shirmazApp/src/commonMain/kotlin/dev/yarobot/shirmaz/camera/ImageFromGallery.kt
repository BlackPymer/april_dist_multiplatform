package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap

@Composable
expect fun getImageFromGallery(onReadyImage: (ImageBitmap) -> Unit)