@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package dev.yarobot.shirmaz.render

import android.annotation.SuppressLint
import android.view.Choreographer
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.ModelViewer
import dev.yarobot.shirmaz.camera.BoneController
import dev.yarobot.shirmaz.camera.Bones
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.LEFT_HIP
import dev.yarobot.shirmaz.platform.LEFT_SHOULDER
import dev.yarobot.shirmaz.platform.PlatformLandmark
import dev.yarobot.shirmaz.platform.PlatformLandmarkType
import dev.yarobot.shirmaz.platform.RIGHT_HIP
import dev.yarobot.shirmaz.platform.RIGHT_SHOULDER
import java.nio.ByteBuffer

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
    private val choreographer: Choreographer = Choreographer.getInstance()

    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        isOpaque = false
    }

    private val modelViewer: ModelViewer =
        ModelViewer(surfaceView = surfaceView, uiHelper = uiHelper)

    private val frameScheduler = FrameCallback()

    private val boneController = BoneController(modelViewer)

    private val lifecycleObserver = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            choreographer.postFrameCallback(frameScheduler)
        }

        override fun onPause(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            choreographer.removeFrameCallback(frameScheduler)
            lifecycle.removeObserver(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onSurfaceAvailable() {
        lifecycle.addObserver(lifecycleObserver)

        setUpModelViewer()
        modelOpen()
    }

    override fun bindBones(
        modelPosition: List<PlatformLandmark>?,
        imageWidth: Float,
        imageHeight: Float
    ) {
        with(boneController) {
            modelPosition?.let { list ->
                if (list.isEmpty()) return@let
                Bones.spine.position = (list.findPoseOf(
                    mark = RIGHT_HIP,
                    pictureWidth = imageWidth,
                    pictureHeight = imageHeight
                ) + list.findPoseOf(
                    mark = LEFT_HIP,
                    pictureWidth = imageWidth,
                    pictureHeight = imageHeight
                )) / 2f

                Bones.rightShoulder.position = list.findPoseOf(
                    mark = RIGHT_SHOULDER,
                    pictureWidth = imageWidth,
                    pictureHeight = imageHeight
                )
                Bones.leftShoulder.position = list.findPoseOf(
                    mark = LEFT_SHOULDER,
                    pictureWidth = imageWidth,
                    pictureHeight = imageHeight
                )
            } ?: println("!!! ERROR: modelPosition is null")
        }
    }

    private fun List<PlatformLandmark>.findPoseOf(
        mark: PlatformLandmarkType,
        pictureWidth: Float,
        pictureHeight: Float
    ): Float3 = Float3(
        x = this[mark].position3D.x / pictureWidth,
        y = this[mark].position3D.y / pictureHeight,
        z = this[mark].position3D.z
    )

    private fun modelOpen() {
        val byteBuffer = ByteBuffer.wrap(model.bytes)
        modelViewer.loadModelGlb(byteBuffer)
        modelViewer.transformToUnitCube()
    }

    private fun setUpModelViewer() = modelViewer.apply {
        scene.skybox = null
        view.blendMode = View.BlendMode.TRANSLUCENT
        renderer.clearOptions = renderer.clearOptions.apply {
            clear = true
        }
        view.renderQuality.hdrColorBuffer = View.QualityLevel.LOW
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(frameTimeNanos)
        }
    }
}

actual class PlatformRendererConfiguration(
    val lifecycle: Lifecycle,
    val surfaceView: SurfaceView
)