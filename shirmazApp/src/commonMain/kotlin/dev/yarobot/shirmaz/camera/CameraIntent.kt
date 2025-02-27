package dev.yarobot.shirmaz.camera

import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.platform.PlatformImage

sealed interface CameraIntent {
    data class RequestCamera(val permissionsController: PermissionsController) : CameraIntent

    data object ChangeCamera : CameraIntent
    data object TakePicture : CameraIntent
    data object OpenGallery : CameraIntent
    data class ChooseShirt(val shirt: Shirt?) : CameraIntent
    data object BackToToolbar : CameraIntent
    data object SaveImage : CameraIntent
    data class OnImageCaptured(val image: PlatformImage) : CameraIntent
}