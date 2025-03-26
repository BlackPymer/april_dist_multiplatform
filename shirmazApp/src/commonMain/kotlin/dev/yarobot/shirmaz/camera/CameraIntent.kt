package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import dev.icerock.moko.permissions.PermissionsController

sealed interface CameraIntent {
    data class RequestCamera(val permissionsController: PermissionsController) : CameraIntent

    data object ChangeCamera : CameraIntent
    data object TakePicture : CameraIntent
    data object OpenGallery : CameraIntent
    data class ChooseShirt(val shirt: Shirt?) : CameraIntent
    data object BackToToolbar : CameraIntent
    data object SaveImage : CameraIntent
    data object OnImageCreated: CameraIntent

    data class SetImage(val imageBitmap: ImageBitmap) : CameraIntent
}