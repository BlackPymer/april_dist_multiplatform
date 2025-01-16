package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.core.language.ScreenState

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val isUnclothes: Boolean
): ScreenState