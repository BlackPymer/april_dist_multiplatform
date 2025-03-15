package dev.yarobot.shirmaz.camera

sealed interface StorageProvideState {
    data object NotGranted: StorageProvideState
    data object Granted: StorageProvideState
}