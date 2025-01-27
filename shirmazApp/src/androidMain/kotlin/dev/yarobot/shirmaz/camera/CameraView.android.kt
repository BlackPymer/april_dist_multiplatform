package dev.yarobot.shirmaz.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
actual fun CameraView(
    onImageCaptured: (PlatformImage) -> Unit
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize(),
        update = {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            preview.surfaceProvider = previewView.surfaceProvider
            val cameraSelector =
                CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                    .build()
            cameraProvider.bindToLifecycle(lifeCycleOwner, cameraSelector, preview)
        })

}