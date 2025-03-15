package dev.yarobot.shirmaz.camera

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
actual fun capturePhoto(): ImageBitmap? {
    val imageBitmapState = remember { mutableStateOf<ImageBitmap?>(null) }

    val viewModel = viewModel { CameraXViewModel() }

    LaunchedEffect(Unit) {
        viewModel.takePicture { bitmap: Bitmap ->
            imageBitmapState.value = bitmap.asImageBitmap()
            println("!!$bitmap")
        }
    }

    return imageBitmapState.value
}