package dev.yarobot.shirmaz.camera

import dev.icerock.moko.permissions.PermissionsController

sealed class CameraIntent{
    data class RequestCamera(val controller: PermissionsController) : CameraIntent()
}