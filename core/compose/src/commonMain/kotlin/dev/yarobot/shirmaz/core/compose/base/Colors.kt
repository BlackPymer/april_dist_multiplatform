package dev.yarobot.shirmaz.core.compose.base

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class ShirmazColors(
    val toolBar: Color,
    val shirtBackground: Color,
    val text: Color
)

val shirmazColors = ShirmazColors(
    toolBar = Color(0xB3131313),
    shirtBackground = Color(0x33131313),
    text = Color(0xFFFFFFFF)
)
val LocalShirmazColors = compositionLocalOf<ShirmazColors> {
    error("No provided colors")
}

