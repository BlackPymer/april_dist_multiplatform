package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.yarobot.shirmaz.core.compose.base.LocalPermissionsController
import org.jetbrains.compose.resources.stringResource
import shirmaz.feature.camera.generated.resources.Res
import shirmaz.feature.camera.generated.resources.camera_not_granted
import shirmaz.feature.camera.generated.resources.camera_request

@Composable
fun CameraScreen() {
    val viewModel = viewModel { CameraViewModel() }
    val state by viewModel.state.collectAsState()
    viewModel.onIntent(CameraIntent.RequestCamera(LocalPermissionsController.current))
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.cameraProvideState) {
            is CameraProvideState.Granted -> {
                CameraView()
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