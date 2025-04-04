package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import dev.yarobot.shirmaz.camera.model.CameraType

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val currentShirt: Shirt?,
    val isSaving: Boolean,
    val currentCamera: CameraType,
    val staticImage: ImageBitmap?,
    val viewCreated: Boolean,
    val appMode: AppMode
)
