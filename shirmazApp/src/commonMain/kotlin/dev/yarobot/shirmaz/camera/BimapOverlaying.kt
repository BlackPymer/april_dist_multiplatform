package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap

expect fun ImageBitmap.overlayAlphaPixels(overlay: ImageBitmap): ImageBitmap

