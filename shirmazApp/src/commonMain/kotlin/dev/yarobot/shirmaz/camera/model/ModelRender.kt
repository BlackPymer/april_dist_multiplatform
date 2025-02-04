package dev.yarobot.shirmaz.camera

import androidx.lifecycle.Lifecycle
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformLandmark

expect class ModelRenderer(surfaceView: Any, lifecycle: Any, model: ThreeDModel) {
    fun onSurfaceAvailable()
    fun bindBones(modelPosition:  List<PlatformLandmark>?)
}