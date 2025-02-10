package dev.yarobot.shirmaz.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
actual fun getScreenHeight(): Float {
    val configuration = LocalConfiguration.current
    return with(LocalDensity.current) { configuration.screenHeightDp.dp.toPx() }
}

@Composable
actual fun getScreenWidth(): Float {
    val configuration = LocalConfiguration.current
    return with(LocalDensity.current) { configuration.screenWidthDp.dp.toPx() }
}
