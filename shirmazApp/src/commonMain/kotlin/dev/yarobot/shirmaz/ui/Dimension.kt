package dev.yarobot.shirmaz.ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShirmazDimension(
    val toolBarHeight: Dp,

    val galleryButton: Dp,

    val takePictureButtonBorderCircle: Dp,
    val takePictureButtonBorderWidth: Dp,
    val takePictureButtonCircle: Dp,

    val carouselButton: Dp,
    val carouselPaddingFromToolbar: Dp,
    val carouselPaddingFromLeftScreenBorder: Dp,
    val carouselPaddingFromSavingPanel:Dp,
    val itemSpacing: Dp,
    val shirtPicture: Dp,
    val carouselButtonFontSize: TextUnit,

    val buttonCornerRadius: Dp,
    val borderThikness: Dp,
    val shirtButtonHeight: Dp,
    val shirtButtonWidth: Dp,

    val savingButtonsHeight: Dp,
    val savingButtonsWidth: Dp,
    val savingButtonsFontSize: TextUnit
)

val shirmazDimension = ShirmazDimension(
    toolBarHeight = 144.dp,

    galleryButton = 72.dp,

    takePictureButtonBorderCircle = 64.dp,
    takePictureButtonBorderWidth = 2.dp,
    takePictureButtonCircle = 54.dp,

    carouselButton = 76.dp,
    carouselPaddingFromToolbar = 24.dp,
    carouselPaddingFromSavingPanel = 0.dp,
    itemSpacing = 12.dp,
    shirtPicture = 45.dp,
    carouselButtonFontSize = 10.sp,
    carouselPaddingFromLeftScreenBorder = 16.dp,

    buttonCornerRadius = 16.dp,
    borderThikness = 2.dp,
    shirtButtonHeight = 76.dp,
    shirtButtonWidth = 65.dp,

    savingButtonsHeight = 54.dp,
    savingButtonsWidth = 182.dp,
    savingButtonsFontSize = 16.sp
)
val LocalShirmazDimension = compositionLocalOf<ShirmazDimension> {
    error("No provided colors")
}
