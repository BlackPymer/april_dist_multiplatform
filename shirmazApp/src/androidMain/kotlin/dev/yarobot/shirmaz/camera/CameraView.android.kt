package dev.yarobot.shirmaz.camera

import android.annotation.SuppressLint
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.platform.PlatformImage
import java.util.concurrent.Executor

@SuppressLint("NewApi")
@Composable
actual fun CameraView(
    onImageCaptured: (PlatformImage) -> Unit,
    modelView: @Composable () -> Unit,
    screenHeight: Float,
    screenWidth: Float
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    val resolutionSelector = ResolutionSelector.Builder()
        .setResolutionStrategy(
            ResolutionStrategy(
                Size(screenWidth.toInt(), screenHeight.toInt()),
                ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
            )
        )
        .build()

    val imageAnalyser = remember {
        ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setResolutionSelector(resolutionSelector)
            .build()
    }
    imageAnalyser.setAnalyzer(context.mainExecutor, onImageCaptured)


    Box {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
            update = {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.surfaceProvider = previewView.surfaceProvider
                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                cameraProvider.bindToLifecycle(
                    lifeCycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyser
                )

            })


        modelView()
    }
}