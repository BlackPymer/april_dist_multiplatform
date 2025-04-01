package dev.yarobot.shirmaz.camera

import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri


actual class GalleryWorker actual constructor() {
    private var bitmap = mutableStateOf<ImageBitmap?>(null)
    private var pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null

    @Composable
    actual fun openImageFromGallery(): ImageBitmap? {
        pickMedia?.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        wasInitializer = true
        return bitmap.value
    }

    @Composable
    actual fun Init() {
        val context = LocalContext.current.contentResolver
        pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { it ->
                val stream = context.openInputStream(it)
                stream?.use { inputStream->
                    val originalBitmap = BitmapFactory.decodeStream(inputStream)
                    bitmap.value = originalBitmap.asImageBitmap()
                }
            }
        }
    }

    actual var wasInitializer: Boolean = false
}