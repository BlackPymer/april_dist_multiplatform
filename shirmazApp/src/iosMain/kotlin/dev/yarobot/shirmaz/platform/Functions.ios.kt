package dev.yarobot.shirmaz.platform

import androidx.compose.runtime.Composable

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenWidth(): Float = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.width.toDp().toPx()
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getScreenHeight(): Float = with(LocalDensity.current) {
    LocalWindowInfo.current.containerSize.height.toDp().toPx()
}