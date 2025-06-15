package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.scale

fun ImageBitmap.resizeTo(width: Int, height: Int): ImageBitmap {
    val bitmap = asAndroidBitmap()
    val resizedBitmap = bitmap.scale(width, height)
    return resizedBitmap.asImageBitmap()
}

