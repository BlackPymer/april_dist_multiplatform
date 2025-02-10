package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformLandmark

expect class ModelRenderer(
    surfaceView: Any, lifecycle: Any, model: ThreeDModel, screenHeight: Float, screenWidth: Float
) {
    fun onSurfaceAvailable()
    fun bindBones(
        modelPosition: List<PlatformLandmark>?,
        imageWidth: Float,
        imageHeight: Float
    )
}