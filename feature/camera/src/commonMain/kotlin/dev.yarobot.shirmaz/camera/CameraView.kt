package dev.yarobot.shirmaz.camera

import androidx.compose.runtime.Composable

@Composable
expect fun CameraView(modelView: @Composable () -> Unit)