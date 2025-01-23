package dev.yarobot.shirmaz.core.compose.base

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShirmazDimension(
    val toolBarHeight: Dp,
    val galleryButton: Dp,
    val takePictureButtonBorderCircle: Dp,
    val takePictureButtonBorderWidth: Dp,
    val takePictureButtonCircle: Dp,
    val carouselButton: Dp,
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
    takePictureButtonBorderCircle = 64.dp,
    takePictureButtonBorderWidth = 2.dp,
    takePictureButtonCircle = 54.dp,
    carouselButton = 72.dp,
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
