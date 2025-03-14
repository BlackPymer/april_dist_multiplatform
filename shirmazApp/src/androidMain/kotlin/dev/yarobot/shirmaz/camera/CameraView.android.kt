package dev.yarobot.shirmaz.camera

import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage

@Composable
actual fun CameraView(
    modifier: Modifier,
    cameraType: CameraType,
    onImageCaptured: (PlatformImage) -> Unit,
    onPictureTaken: (PlatformImage) -> Unit,
    capturePhotoStarted: Boolean,
    imageToDisplay: PlatformImage?
) {
    val viewModel = viewModel { CameraXViewModel() }
    val context = LocalContext.current

    viewModel.setAnalyzeUseCase(onImageCaptured)
    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()
    LaunchedEffect(capturePhotoStarted) {
        if (capturePhotoStarted) {
            viewModel.takePicture(onPictureTaken)
        }
    }
    LaunchedEffect(lifecycleOwner, cameraType) {
        viewModel.bindToCamera(
            context.applicationContext,
            lifecycleOwner,
            when (cameraType) {
                CameraType.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                CameraType.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            }
        )
    }
    surfaceRequest?.let { request ->
        CameraXViewfinder(surfaceRequest = request)
    }
}

