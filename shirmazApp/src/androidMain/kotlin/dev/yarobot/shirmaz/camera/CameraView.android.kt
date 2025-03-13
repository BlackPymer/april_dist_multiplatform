package dev.yarobot.shirmaz.camera

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.platform.PlatformImage
import java.util.Date
import java.util.Locale

@Composable
actual fun CameraView(
    modifier: Modifier,
    cameraType: CameraType,
    onImageCaptured: (PlatformImage) -> Unit,
    modelView: @Composable BoxScope.() -> Unit,
    imageToDisplay: PlatformImage?
) {
    val viewModel = viewModel { CameraXViewModel() }
    val context = LocalContext.current
    viewModel.setAnalyzeUseCase(onImageCaptured)
    val lifecycleOwner = LocalLifecycleOwner.current
    val surfaceRequest by viewModel.surfaceRequest.collectAsStateWithLifecycle()

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
    Box(modifier) {
        surfaceRequest?.let { request ->
            CameraXViewfinder(surfaceRequest = request)
        }
        modelView()
    }
}

