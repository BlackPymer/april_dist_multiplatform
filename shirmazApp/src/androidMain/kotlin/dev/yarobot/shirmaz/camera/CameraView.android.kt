package dev.yarobot.shirmaz.camera

import android.annotation.SuppressLint
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.compose.foundation.layout.Box
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

@SuppressLint("NewApi")
@Composable
actual fun CameraView(
    modifier: Modifier,
    cameraType: CameraType,
    onImageCaptured: (image: PlatformImage) -> Unit,
    modelView: @Composable () -> Unit,
    screenHeight: Float,
    screenWidth: Float
) {
    val viewModel = viewModel {
        CameraXViewModel(
            screenHeight = screenHeight.toInt(),
            screenWidth = screenWidth.toInt()
        )
    }
    val context = LocalContext.current

    modelView()

    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()

    LaunchedEffect(lifecycleOwner, cameraType) {
        viewModel.bindToCamera(
            context.applicationContext, lifecycleOwner,
            when (cameraType) {
                CameraType.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                CameraType.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            }
        )
    }
    Box(modifier) {
        surfaceRequest?.let { request ->
            CameraXViewfinder(surfaceRequest = request)
        }
    }
    viewModel.cameraAnalyzeUseCase.setAnalyzer(context.mainExecutor,onImageCaptured)
}