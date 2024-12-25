package dev.yarobot.shirmaz.camera

import android.annotation.SuppressLint
import android.view.Choreographer
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.ModelViewer
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import java.nio.ByteBuffer

class ModelRenderer(
    private val surfaceView: SurfaceView,
    private val lifecycle: Lifecycle,
    private val model: ThreeDModel
) {
    private val choreographer: Choreographer = Choreographer.getInstance()
    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        isOpaque = false
    }

    private val modelViewer: ModelViewer = ModelViewer(surfaceView = surfaceView, uiHelper = uiHelper)

    private val frameScheduler = FrameCallback()


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
    fun onSurfaceAvailable() {
        lifecycle.addObserver(lifecycleObserver)

        surfaceView.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }

        setUpModelViewer()
        modelOpen()
    }

    private fun modelOpen() {
        val byteBuffer = ByteBuffer.wrap(model.bytes)
        modelViewer.loadModelGlb(byteBuffer)
        modelViewer.transformToUnitCube()
    }

    private fun setUpModelViewer() = modelViewer.apply {
        scene.skybox = null
        view.blendMode = View.BlendMode.TRANSLUCENT
        renderer.clearOptions = this.renderer.clearOptions.apply {
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
