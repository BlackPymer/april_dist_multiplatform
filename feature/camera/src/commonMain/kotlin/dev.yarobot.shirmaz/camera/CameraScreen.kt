package dev.yarobot.shirmaz.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import org.jetbrains.compose.resources.stringResource
import shirmaz.feature.camera.generated.resources.Res
import shirmaz.feature.camera.generated.resources.camera_not_granted
import shirmaz.feature.camera.generated.resources.camera_request

@Composable
fun CameraScreen() {
    ScreenContent()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScreenContent() {
    val cameraPermissionState = rememberPermissionState(
        Permission.Camera
    )
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        when(cameraPermissionState.status){
            is PermissionStatus.Granted -> {
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
                        onClick = { cameraPermissionState.launchPermissionRequest() }
                    ){
                        Text(text = stringResource(Res.string.camera_request))
                    }
                }
            }
        }

    }
}