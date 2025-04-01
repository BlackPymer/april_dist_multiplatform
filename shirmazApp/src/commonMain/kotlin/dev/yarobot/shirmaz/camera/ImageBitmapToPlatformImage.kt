package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import dev.yarobot.shirmaz.platform.PlatformImage

expect fun ImageBitmap.toInputImage(): PlatformImage