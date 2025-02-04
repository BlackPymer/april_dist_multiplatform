package dev.yarobot.shirmaz.camera.model

import androidx.compose.runtime.Composable
import dev.yarobot.shirmaz.camera.CameraScreenState

@Composable
expect fun ModelView(state: CameraScreenState)