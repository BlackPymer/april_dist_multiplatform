package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.core.compose.base.LocalPermissionsController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import shirmaz.feature.camera.generated.resources.Res
import shirmaz.feature.camera.generated.resources.camera_not_granted
import shirmaz.feature.camera.generated.resources.camera_request
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.painter.Painter
import shirmaz.feature.camera.generated.resources.gallery
import shirmaz.feature.camera.generated.resources.gallery_cd


@Composable
fun CameraScreen() {
    val viewModel = viewModel { CameraViewModel() }
    val state by viewModel.state.collectAsState()
    ScreenContent(
        onIntent = { viewModel.onIntent(it) },
        state = remember(state) { state }
    )
}

@Composable
private fun ScreenContent(
    onIntent: (CameraIntent) -> Unit,
    state: CameraScreenState
) {
    val permissionsController = LocalPermissionsController.current
    LaunchedEffect(permissionsController) {
        onIntent(CameraIntent.RequestCamera(permissionsController))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.cameraProvideState) {
            is CameraProvideState.Granted -> {
                CameraView()
                ToolBar(onIntent = onIntent)
            }

            else -> {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(Res.string.camera_not_granted))
                    TextButton(
                        onClick = { onIntent(CameraIntent.RequestCamera(permissionsController)) }
                    ) {
                        Text(text = stringResource(Res.string.camera_request))
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.ToolBar(onIntent: (CameraIntent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(144.dp)
            .align(Alignment.BottomCenter)
            .background(Color.Black.copy(alpha = 0.5f)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GalleryButton(onIntent = onIntent)
        TakePictureButton(onIntent = onIntent)
    }
}

@Composable
private fun GalleryButton(modifier: Modifier = Modifier, onIntent: (CameraIntent) -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = { onIntent(CameraIntent.OpenGallery) }

    ) {

        val painter: Painter = painterResource(Res.drawable.gallery)
        Image(
            painter = painter,
            contentDescription = stringResource(Res.string.gallery_cd),
            modifier = Modifier.size(48.dp)
        )
    }
}

@Composable
private fun TakePictureButton(
    modifier: Modifier = Modifier,
    onIntent: (CameraIntent) -> Unit
) {
    IconButton(
        onClick = { onIntent(CameraIntent.TakePicture) }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}