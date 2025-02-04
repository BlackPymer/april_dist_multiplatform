package dev.yarobot.shirmaz.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class ShirmazColors(
    val toolBar: Color,
    val shirtBackground: Color,
    val text: Color,
    val takePictureButton: Color
)

internal val shirmazColors = ShirmazColors(
    toolBar = Color(0xB3131313),
    shirtBackground = Color(0x33131313),
    text = Color(0xFFFFFFFF),
    takePictureButton = Color(0xFFFFFFFF)
)


