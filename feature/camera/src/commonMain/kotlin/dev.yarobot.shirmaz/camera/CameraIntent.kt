package dev.yarobot.shirmaz.camera

import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.core.language.Intent

sealed class CameraIntent : Intent {
    data class RequestCamera(val permissionsController: PermissionsController) : CameraIntent()
    data class CheckCameraPermission(val permissionsController: PermissionsController) :
        CameraIntent()
    data object TakePicture : CameraIntent()
    data object OpenGallery : CameraIntent()
    data object Unclothes: CameraIntent()
}