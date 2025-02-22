@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package dev.yarobot.shirmaz.render

import android.annotation.SuppressLint
import android.view.SurfaceView
import androidx.lifecycle.Lifecycle
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformLandmark

actual fun createModelRenderer(
    model: ThreeDModel,
    platformRendererConfiguration: PlatformRendererConfiguration
): ModelRenderer {
    return AndroidModelRenderer(
        surfaceView = platformRendererConfiguration.surfaceView,
        lifecycle = platformRendererConfiguration.lifecycle,
        model = model
    )
}

private class AndroidModelRenderer(
    val surfaceView: SurfaceView,
    val lifecycle: Lifecycle,
    override val model: ThreeDModel,
) : ModelRenderer {


    @SuppressLint("ClickableViewAccessibility")
    override fun onSurfaceAvailable() {

    }

    override fun bindBones(
        modelPosition: List<PlatformLandmark>?,
        imageWidth: Float,
        imageHeight: Float
    ) {
    }
}

actual class PlatformRendererConfiguration(
    val lifecycle: Lifecycle,
    val surfaceView: SurfaceView
)