package dev.yarobot.shirmaz.camera

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File

fun savePhoto(imageBitmap: ImageBitmap) {
    val context = provideAppContext()
    val bitmap: Bitmap = imageBitmap.asAndroidBitmap()
    val resolver = context.contentResolver

    val fileName = "shirmaz_${System.currentTimeMillis()}.jpg"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Shirmaz")
        }
    }

    val imageUri: Uri? =
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    if (imageUri != null) {
        resolver.openOutputStream(imageUri)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }


        val filePath: String? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            File(dcimDir, "Shirmaz/$fileName").absolutePath
        } else {
            getFilePathFromUri(imageUri, context)
        }

        filePath?.let { path ->
            MediaScannerConnection.scanFile(
                context,
                arrayOf(path),
                null
            ) { scannedPath, scannedUri ->
                println("Scanned file: $scannedPath")
                println("-> URI: $scannedUri")
            }
        }
    }
}


private fun getFilePathFromUri(uri: Uri, context: Context): String? {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (cursor.moveToFirst()) {
            return cursor.getString(columnIndex)
        }
    }
    return null
}

lateinit var appContext: Context

fun initAppContext(context: Context) {
    appContext = context
}

fun provideAppContext(): Context = appContext