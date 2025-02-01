package dev.yarobot.shirmaz.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import androidx.lifecycle.compose.LocalLifecycleOwner
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
actual fun CameraView(
    onImageCaptured: (PlatformImage) -> Unit,
    modelView: @Composable () -> Unit,
) {

    Box {
        val context = LocalContext.current
        val lifeCycleOwner = LocalLifecycleOwner.current

        val previewView = remember { PreviewView(context) }

        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

        val imageCapture = remember { ImageCapture.Builder().build() }

        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                view.doOnAttach {
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build()
                        preview.surfaceProvider = previewView.surfaceProvider

                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()

                        imageCapture.targetRotation = previewView.display.rotation

                        cameraProvider.bindToLifecycle(
                            lifeCycleOwner,
                            cameraSelector,
                            imageCapture,
                            preview
                        )

                        imageCapture.takePicture(
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageCapturedCallback() {
                                override fun onCaptureSuccess(image: PlatformImage) {
                                    onImageCaptured(image)
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    println("!!Error in image capturing: $exception")
                                }
                            }
                        )
                    }, ContextCompat.getMainExecutor(context))
                }
            }
        )
        modelView()
    }}