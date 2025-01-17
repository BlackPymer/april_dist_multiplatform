package dev.yarobot.shirmaz.core.compose.base

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class ShirmazColors(
    val toolBar: Color
)

val shirmazColors = ShirmazColors(
    toolBar = Color(0xB3131313)
)
val LocalShirmazColors = compositionLocalOf<ShirmazColors> {
    error("No provided colors")
}

