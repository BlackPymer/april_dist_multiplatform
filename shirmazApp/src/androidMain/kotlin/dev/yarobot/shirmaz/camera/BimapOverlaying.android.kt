package dev.yarobot.shirmaz.camera

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.get
import androidx.core.graphics.set

fun ImageBitmap.overlayAlphaPixels(overlay: ImageBitmap): ImageBitmap {
    require(this.width == overlay.width && this.height == overlay.height) {
        "Both image bitmaps must have the same size"
    }
    val bitmap = overlay.asAndroidBitmap()
    val result = this.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, true)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val pixel = bitmap[x, y]
            val alpha = Color.alpha(pixel)
            if (alpha != 0) {
                result[x, y] = pixel
            }
        }
    }

    return result.asImageBitmap()
}