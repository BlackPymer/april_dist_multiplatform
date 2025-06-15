package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.mlkit.vision.common.InputImage
import dev.yarobot.shirmaz.platform.PlatformInputImage

fun ImageBitmap.toPlatformInputImage(): PlatformInputImage {
    return InputImage.fromBitmap(this.asAndroidBitmap(), 0)
}