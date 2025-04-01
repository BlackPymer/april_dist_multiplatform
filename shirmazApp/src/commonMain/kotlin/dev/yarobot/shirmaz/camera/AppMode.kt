package dev.yarobot.shirmaz.camera

sealed interface AppMode {
    data object CameraMode: AppMode
    data object GalleryMode: AppMode
}