package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.core.language.ScreenState

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val shirts: List<Shirt>,
    val currentShirt: Shirt?,
    val currentModel: ThreeDModel?,
    val isCarouselVisible: Boolean,
    val saving: Boolean
) : ScreenState
