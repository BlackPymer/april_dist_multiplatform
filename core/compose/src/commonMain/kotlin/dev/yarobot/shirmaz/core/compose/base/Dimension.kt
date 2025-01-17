package dev.yarobot.shirmaz.core.compose.base

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShirmazDimension(
    val toolBarHeight: Dp,
    val galleryButton: Dp,
    val takePictureButton: Dp,
    val unclothesButton: Dp,
    val clothesButton: Dp,
    val shirtButton: Dp,
    val caruselPaddingFromToolbar: Dp,
    val itemSpacing: Dp,
    val buttonCornerRadius: Dp,
    val borderThikness: Dp
)

val shirmazDimension = ShirmazDimension(
    toolBarHeight = 144.dp,
    galleryButton = 72.dp,
    takePictureButton = 100.dp,
    unclothesButton = 72.dp,
    clothesButton = 72.dp,
    shirtButton = 100.dp,
    caruselPaddingFromToolbar = 16.dp,
    itemSpacing = 16.dp,
    buttonCornerRadius = 16.dp,
    borderThikness = 2.dp
)
val LocalShirmazDimension = compositionLocalOf<ShirmazDimension> {
    error("No provided colors")
}
