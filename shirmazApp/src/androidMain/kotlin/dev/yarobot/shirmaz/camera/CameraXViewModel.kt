package dev.yarobot.shirmaz.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import android.util.Size
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import dev.yarobot.shirmaz.camera.model.CameraSize
import dev.yarobot.shirmaz.platform.PlatformImage
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.Executors

private val backgroundExecutor = Executors.newSingleThreadExecutor()

class CameraXViewModel() : ViewModel() {
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private val cameraPreviewUseCase = Preview.Builder()
        .build().apply {
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

    private val imageCaptureUseCase = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    private val cameraAnalyzeUseCase = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .setResolutionSelector(resolutionSelector)
        .build()

    @OptIn(ExperimentalGetImage::class)
    fun setAnalyzeUseCase(analyzer: (PlatformImage) -> Unit) {
        cameraAnalyzeUseCase.setAnalyzer(backgroundExecutor, analyzer)
    }

    fun takePicture(onTakenPicture: (Bitmap) -> Unit) {
        imageCaptureUseCase.takePicture(
            backgroundExecutor,
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                    val rotatedImage = imageProxy.toBitmap().rotate(rotationDegrees)
                    onTakenPicture(rotatedImage)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e(
                        "CameraXViewModel",
                        "Image capture failed: ${exception.message}",
                        exception
                    )
                }
            }
        )
    }

    private fun Bitmap.rotate(degrees: Int): Bitmap {
        return if (degrees != 0) {
            val matrix = Matrix().apply {
                postRotate(degrees.toFloat())
            }
            Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
        } else {
            this
        }
    }

    private val useCaseGroup = UseCaseGroup.Builder()
        .addUseCase(cameraPreviewUseCase)
        .addUseCase(imageCaptureUseCase)
        .addUseCase(cameraAnalyzeUseCase)
        .build()

    suspend fun bindToCamera(
        appContext: Context,
        lifecycleOwner: LifecycleOwner,
        cameraSelector: CameraSelector
    ) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)

        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            useCaseGroup
        )
        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }
}