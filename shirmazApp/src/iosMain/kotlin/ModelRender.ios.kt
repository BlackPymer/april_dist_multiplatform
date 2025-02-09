package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformLandmark

actual class ModelRenderer actual constructor(surfaceView: Any, lifecycle: Any, model: ThreeDModel) {
    actual fun onSurfaceAvailable() {
        throw NotImplementedError("ModelRenderer is not implemented for iOS.")
    }
    actual fun bindBones(
        modelPosition: List<PlatformLandmark>?,
        screenWidth: Float,
        screenHeight: Float
    )  {
        throw NotImplementedError("ModelRenderer is not implemented for iOS.")
    }
}