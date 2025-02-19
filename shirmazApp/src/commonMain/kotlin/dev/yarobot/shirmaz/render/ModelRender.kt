@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package dev.yarobot.shirmaz.render

import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformLandmark

interface ModelRenderer{
    val model: ThreeDModel

    fun onSurfaceAvailable()
    fun bindBones(
        modelPosition: List<PlatformLandmark>?,
        imageWidth: Float,
        imageHeight: Float
    )
}

expect class PlatformRendererConfiguration

expect fun createModelRenderer(
    model: ThreeDModel,
    platformRendererConfiguration: PlatformRendererConfiguration
): ModelRenderer