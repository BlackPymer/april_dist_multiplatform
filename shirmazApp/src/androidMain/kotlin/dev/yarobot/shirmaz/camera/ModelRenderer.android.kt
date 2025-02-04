// androidMain/src/your/package/ModelRenderer.kt

package dev.yarobot.shirmaz.camera

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
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import java.nio.ByteBuffer
import androidx.lifecycle.LifecycleObserver
import dev.yarobot.shirmaz.platform.PlatformLandmark

actual class ModelRenderer actual constructor(
    surfaceView: Any,
    lifecycle: Any,
    private val model: ThreeDModel
) {

    private val surfaceView1: SurfaceView = surfaceView as SurfaceView
    private val lifecycle1: Lifecycle = lifecycle as Lifecycle

    private val choreographer: Choreographer = Choreographer.getInstance()

    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        isOpaque = false
    }

    private val modelViewer: ModelViewer =
        ModelViewer(surfaceView = surfaceView1, uiHelper = uiHelper)

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
            lifecycle1.removeObserver(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    actual fun onSurfaceAvailable() {
        lifecycle1.addObserver(lifecycleObserver)

        surfaceView1.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }

        setUpModelViewer()
        modelOpen()

        /*
        with(boneController) {
            Bones.leftShoulder.position = Float3(100f, 0f, 0f)
            Bones.spine.position = Float3(0f, 100f, 0f)
            Bones.leftShoulder.rotation = Float3(45f, 0f, 0f)
        }
        */
    }

    actual fun bindBones(modelPosition:  List<PlatformLandmark>?){
        with(boneController){
            Bones.spine.position = modelPosition?.let { list ->
                if (list.size > 24) {
                    val pos1 = list[23].position
                    val pos2 = list[24].position
                    Float3(
                        x = (pos1.x + pos2.x) / 2,
                        y = (pos1.y + pos2.y) / 2
                        /*,
                        z = (pos1.z + pos2.z) / 2*/
                    )
                } else {
                    null
                }
            }

        }
    }

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
        view.renderQuality.hdrColorBuffer = View.QualityLevel.MEDIUM
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(frameTimeNanos)
        }
    }
}
