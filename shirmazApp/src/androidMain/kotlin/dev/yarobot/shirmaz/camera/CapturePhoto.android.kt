package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.platform.ActualContext
import dev.yarobot.shirmaz.platform.CaptureImage
import dev.yarobot.shirmaz.platform.URI
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.work.await
import com.google.android.filament.Box
import dev.yarobot.shirmaz.platform.PlatformImage
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.Locale
actual fun capturePhoto(
    context: ActualContext,
    imageCapture: CaptureImage,
    onImageCaptured: (URI) -> Unit
) {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "JPEG_$timeStamp.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }
    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(
            context.androidContext.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        .build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context.androidContext),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                Log.d("capturePhoto", "Изображение сохранено: $savedUri")
                onImageCaptured(URI.fromAndroidUri(savedUri ?: Uri.EMPTY))
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("capturePhoto", "Ошибка при захвате изображения", exception)
            }
        }
    )
}

