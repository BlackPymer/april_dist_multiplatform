package dev.yarobot.shirmaz.camera

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import dev.yarobot.shirmaz.camera.model.CameraSize
import dev.yarobot.shirmaz.platform.PlatformImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executor

class CameraXViewModel() : ViewModel() {
    private var cameraProvider: ProcessCameraProvider? = null

    private val handlerThread = HandlerThread("CameraXBackgroundThread").apply { start() }

    private val backgroundExecutor = object : Executor {
        private val handler = Handler(handlerThread.looper)
        override fun execute(command: Runnable?) {
            command?.let { handler.post(it) }
        }
    }

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    private val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(
            ResolutionStrategy(
                Size(CameraSize.WIDTH, CameraSize.HEIGHT),
                ResolutionStrategy.FALLBACK_RULE_NONE
            )
        )
        .build()

    private val cameraAnalyzeUseCase = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .setResolutionSelector(resolutionSelector)
        .build()

    fun setAnalyzeUseCase(analyzer: (PlatformImage) -> Unit) {
        cameraAnalyzeUseCase.setAnalyzer(backgroundExecutor, analyzer)
    }

    suspend fun bindToCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector
    ) {
        ProcessCameraProvider.awaitInstance(appContext).apply {
            cameraProvider = this
            try {
                unbindAll()
                bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    cameraPreviewUseCase,
                    cameraAnalyzeUseCase
                )
            } catch (e: Exception) {
                Log.e("Camera", "Use case binding failed", e)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handlerThread.quitSafely()
    }
}