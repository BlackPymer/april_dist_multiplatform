package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
expect fun PlatformImage.toImageBitmap(): ImageBitmap