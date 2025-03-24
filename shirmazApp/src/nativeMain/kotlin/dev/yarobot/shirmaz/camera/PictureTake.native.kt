package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.render.ModelView

@Composable
actual fun TakeShirtPicture(
    shirt: ThreeDModel,
    modelView: ModelView,
    onBitmapReady: (ImageBitmap) -> Unit
){
    TODO("Not yet implemented")
}