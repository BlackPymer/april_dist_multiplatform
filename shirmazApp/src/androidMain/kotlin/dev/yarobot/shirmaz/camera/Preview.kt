package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.ui.ShirmazTheme

@Preview(showBackground = true)
@Composable
private fun PreviewGranted() {
    ShirmazTheme {
        ScreenContent(
            state = CameraScreenState(
                cameraProvideState = CameraProvideState.Granted,
                currentModel = null,
                currentShirt = null,
                currentCamera = CameraType.FRONT,
                saving = false,
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDenied() {
    ShirmazTheme {
        ScreenContent(
            state = CameraScreenState(
                cameraProvideState = CameraProvideState.NotGranted,
                currentModel = null,
                currentShirt = null,
                currentCamera = CameraType.FRONT,
                saving = false,
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSaving() {
    ShirmazTheme {
        ScreenContent(
            state = CameraScreenState(
                cameraProvideState = CameraProvideState.Granted,
                currentModel = null,
                currentShirt = null,
                saving = true,
                currentCamera = CameraType.FRONT
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithShirts() {
    ShirmazTheme {
        ScreenContent(
            state = CameraScreenState(
                cameraProvideState = CameraProvideState.Granted,
                currentModel = null,
                currentShirt = null,
                currentCamera = CameraType.FRONT,
                saving = false,
            ),
            onIntent = {}
        )
    }
}
