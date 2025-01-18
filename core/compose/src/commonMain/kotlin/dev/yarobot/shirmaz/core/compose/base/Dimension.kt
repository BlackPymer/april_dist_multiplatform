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
    val shirtPicture: Dp,
    val carouselPaddingFromToolbar: Dp,
    val itemSpacing: Dp,
    val buttonCornerRadius: Dp,
    val borderThikness: Dp,
    val shirtButtonHeight: Dp,
    val shirtButtonWidth: Dp
)

val shirmazDimension = ShirmazDimension(
    toolBarHeight = 144.dp,
    galleryButton = 72.dp,
    takePictureButton = 100.dp,
    unclothesButton = 72.dp,
    clothesButton = 72.dp,
    shirtPicture = 45.dp,
    carouselPaddingFromToolbar = 16.dp,
    itemSpacing = 16.dp,
    buttonCornerRadius = 16.dp,
    borderThikness = 2.dp,
    shirtButtonHeight = 76.dp,
    shirtButtonWidth = 65.dp
)
val LocalShirmazDimension = compositionLocalOf<ShirmazDimension> {
    error("No provided colors")
}
