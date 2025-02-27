package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformLandmark

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val currentModel: ThreeDModel?,
    val modelPosition:  List<PlatformLandmark>?
    val shirts: List<Shirt>,
    val currentShirt: Shirt?,
    val saving: Boolean,
    val currentCamera: CameraType
)
