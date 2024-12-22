package dev.yarobot.shirmaz.camera

import android.annotation.SuppressLint
import android.view.Choreographer
import android.view.SurfaceView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.filament.View
import com.google.android.filament.android.UiHelper
import com.google.android.filament.utils.ModelViewer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import shirmaz.feature.camera.generated.resources.Res
import java.nio.ByteBuffer


class ModelRenderer(
    private val surfaceView: SurfaceView,
    private val lifecycle: Lifecycle) {


    private val dispatcher = Dispatchers.Unconfined
    private val modelOpenScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val choreographer: Choreographer = Choreographer.getInstance()
    private val uiHelper: UiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK).apply {
        // This is needed to make the background transparent
        isOpaque = false
    }

    private val modelViewer: ModelViewer =
        ModelViewer(surfaceView = surfaceView, uiHelper = uiHelper)

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

        // This is needed so we can move the camera in the rendering
        surfaceView.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }

        // This is the other code needed to make the background transparent
        modelViewer.scene.skybox = null
        modelViewer.view.blendMode = View.BlendMode.TRANSLUCENT
        modelViewer.renderer.clearOptions = modelViewer.renderer.clearOptions.apply {
            clear = true
        }

        // This part defines the quality of your model, feel free to change it or
        // add other options
        modelViewer.view.apply {
            renderQuality = renderQuality.apply {
                hdrColorBuffer = View.QualityLevel.MEDIUM
            }
        }
        modelOpenScope.launch {
            modelOpen()
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun modelOpen() {
        val buffer = Res.readBytes("files/sample1.glb")
        val byteBuffer = ByteBuffer.wrap(buffer)
        modelViewer.loadModelGlb(byteBuffer)
        modelViewer.transformToUnitCube()
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)
            modelViewer.render(frameTimeNanos)
        }
    }
}
