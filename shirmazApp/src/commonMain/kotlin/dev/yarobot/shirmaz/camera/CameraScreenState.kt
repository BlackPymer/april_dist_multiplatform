package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.camera.model.ThreeDModel

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val storageProvideState: StorageProvideState,
    val currentModel: ThreeDModel?,
    val currentShirt: Shirt?,
    val saving: Boolean,
    val currentCamera: CameraType

)
