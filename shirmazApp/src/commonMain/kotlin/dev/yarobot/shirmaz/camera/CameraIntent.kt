package dev.yarobot.shirmaz.camera

import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.platform.PlatformImage

sealed class CameraIntent{
    data class RequestCamera(val controller: PermissionsController) : CameraIntent()
    data class OnImageCaptured(val image: PlatformImage) : CameraIntent()
}