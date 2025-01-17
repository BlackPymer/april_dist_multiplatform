package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.core.language.ScreenState
import shirmaz.feature.camera.generated.resources.Res

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,

    val isUnclothes: Boolean,
    val shirts: Array<Shirt>,
    val chosenShirt: Int

    val currentModel: ThreeDModel?
): ScreenState
