package dev.yarobot.shirmaz.camera

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap


actual fun ImageBitmap.resizeTo(width: Int, height: Int): ImageBitmap {
    val bitmap = asAndroidBitmap()
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
    return resizedBitmap.asImageBitmap()
}

