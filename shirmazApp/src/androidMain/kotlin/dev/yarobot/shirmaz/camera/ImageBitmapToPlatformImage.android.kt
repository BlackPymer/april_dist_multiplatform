package dev.yarobot.shirmaz.camera

import androidx.camera.core.ImageProxy
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import com.google.mlkit.vision.common.InputImage
import dev.yarobot.shirmaz.platform.PlatformImage



actual fun ImageBitmap.toInputImage(): PlatformImage {
    return InputImage.fromBitmap(this.asAndroidBitmap(), 0)
}