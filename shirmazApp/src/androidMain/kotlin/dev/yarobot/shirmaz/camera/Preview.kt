package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.ui.ShirmazTheme
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.clothes
import shirmaz.shirmazapp.generated.resources.jacket_name
import shirmaz.shirmazapp.generated.resources.mia_name

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
                shirts = listOf()
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
                shirts = listOf()
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
                shirts = listOf(),
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
        val shirt = Shirt(Res.string.mia_name, Res.drawable.clothes, "Shirt 1")
        val shirt2 = shirt.copy(nameRes = Res.string.jacket_name, modelName = "Shirt 2")
        val shirt3 = shirt.copy(nameRes = Res.string.jacket_name, modelName = "Shirt 3")
        ScreenContent(
            state = CameraScreenState(
                cameraProvideState = CameraProvideState.Granted,
                currentModel = null,
                currentShirt = null,
                currentCamera = CameraType.FRONT,
                saving = false,
                shirts = listOf(shirt, shirt2, shirt3)
            ),
            onIntent = {}
        )
    }
}
