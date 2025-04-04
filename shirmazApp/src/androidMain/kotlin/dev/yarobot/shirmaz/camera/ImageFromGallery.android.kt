package dev.yarobot.shirmaz.camera

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
fun getImageFromGallery(onReadyImage: (ImageBitmap) -> Unit) {
    val contentResolver = LocalContext.current.contentResolver
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let { it ->
            val stream = contentResolver.openInputStream(it)
            stream?.use { inputStream->
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                onReadyImage(originalBitmap.asImageBitmap())
            }
        }
    }
    SideEffect {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}