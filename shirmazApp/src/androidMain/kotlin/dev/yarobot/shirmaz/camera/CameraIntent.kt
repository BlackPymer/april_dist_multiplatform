package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import dev.icerock.moko.permissions.PermissionsController

sealed interface CameraIntent {
    data class RequestCamera(val permissionsController: PermissionsController) : CameraIntent

    data object ChangeCamera : CameraIntent
    data object TakePicture : CameraIntent
    data class ChooseShirt(val shirt: Shirt?) : CameraIntent
    data object BackToCamera : CameraIntent
    data object SaveImage : CameraIntent
    data object OnImageCreated : CameraIntent
    data object ViewCreated : CameraIntent

    data class SetImage(val imageBitmap: ImageBitmap) : CameraIntent
}