package dev.yarobot.shirmaz.camera

sealed interface CameraProvideState{
    data object NotGranted: CameraProvideState
    data object Granted: CameraProvideState
}