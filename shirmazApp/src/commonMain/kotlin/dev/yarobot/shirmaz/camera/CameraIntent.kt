package dev.yarobot.shirmaz.camera

import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.platform.PlatformImage

sealed class CameraIntent{
    data class RequestCamera(val permissionsController: PermissionsController) : CameraIntent()
    data class CheckCameraPermission(val permissionsController: PermissionsController) :
        CameraIntent()
    data class OpenSettings(val permissionsController: PermissionsController) : CameraIntent()
    data class OnImageCaptured(val image: PlatformImage) : CameraIntent()
}