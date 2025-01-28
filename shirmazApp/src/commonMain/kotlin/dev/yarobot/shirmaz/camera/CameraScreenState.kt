package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.ThreeDModel

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val currentModel: ThreeDModel?
)