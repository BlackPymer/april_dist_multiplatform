package dev.yarobot.shirmaz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.yarobot.shirmaz.camera.CameraScreen
import dev.yarobot.shirmaz.core.compose.ui.ShirmazTheme

@Composable
fun App() = ShirmazTheme {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CameraScreen()
    }
}
