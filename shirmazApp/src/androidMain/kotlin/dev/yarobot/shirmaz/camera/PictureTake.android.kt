package dev.yarobot.shirmaz.camera

import android.graphics.Bitmap
import android.graphics.Picture
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.render.ModelView

@Composable
actual fun TakeShirtPicture(
    shirt: ThreeDModel,
    modelView: ModelView,
    onBitmapReady: (ImageBitmap) -> Unit
) {
    val picture = remember { Picture() }
    var bitmapEmitted by remember { mutableStateOf(false) }

    modelView.ModelRendererInit(
        shirt,
        Modifier.drawWithCache {
            val width = this.size.width.toInt()
            val height = this.size.height.toInt()
            println("!!takeShirtPicture width: $width, height: $height")

            onDrawWithContent {
                this@onDrawWithContent.drawContent()

                if (width > 0 && height > 0 && !bitmapEmitted) {
                    val pictureCanvas = androidx.compose.ui.graphics.Canvas(
                        picture.beginRecording(width, height)
                    )
                    draw(this, this.layoutDirection, pictureCanvas, this.size) {
                        this@onDrawWithContent.drawContent()
                    }
                    picture.endRecording()

                    val bitmap = Bitmap.createBitmap(
                        width,
                        height,
                        Bitmap.Config.ARGB_8888
                    )
                    val bmpCanvas = android.graphics.Canvas(bitmap)
                    bmpCanvas.drawPicture(picture)

                    onBitmapReady(bitmap.asImageBitmap())
                    bitmapEmitted = true
                }
            }
        }
    )
}
