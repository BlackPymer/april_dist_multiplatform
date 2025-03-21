package dev.yarobot.shirmaz.ui

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ShirmazDimension(
    val toolBarHeight: Dp,
    val savingPanelHeight: Dp,

    val takePictureButtonBorderCircle: Dp,
    val takePictureButtonBorderWidth: Dp,
    val takePictureButtonCircle: Dp,

    val sideCarouselButtonSize: Dp,
    val carouselPaddingBottom: Dp,
    val carouselPaddingLeft: Dp,
    val itemSpacing: Dp,
    val shirtPicture: Dp,

    val buttonCornerRadius: Dp,
    val borderThikness: Dp,
    val shirtButtonHeight: Dp,
    val shirtButtonWidth: Dp,

    val savingButtonsHeight: Dp,
    val savingButtonsWidth: Dp,

    val mirrorHeight: Dp
)

internal val shirmazDimension = ShirmazDimension(
    toolBarHeight = 144.dp,
    savingPanelHeight = 104.dp,

    takePictureButtonBorderCircle = 64.dp,
    takePictureButtonBorderWidth = 2.dp,
    takePictureButtonCircle = 54.dp,

    sideCarouselButtonSize = 32.dp,
    carouselPaddingBottom = 24.dp,
    itemSpacing = 12.dp,
    shirtPicture = 45.dp,
    carouselPaddingLeft = 16.dp,

    buttonCornerRadius = 16.dp,
    borderThikness = 2.dp,
    shirtButtonHeight = 76.dp,
    shirtButtonWidth = 65.dp,

    savingButtonsHeight = 54.dp,
    savingButtonsWidth = 182.dp,

    mirrorHeight = 480.dp
)

