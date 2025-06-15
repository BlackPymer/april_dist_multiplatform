package dev.yarobot.shirmaz.camera

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.yarobot.shirmaz.R
import dev.yarobot.shirmaz.camera.model.Models

data class Shirt(
    @StringRes val nameRes: Int,
    @DrawableRes val painterRes: Int,
    val modelName: String
)

val shirts = listOf(
    Shirt(
        nameRes = R.string.red_t_shirt_name,
        painterRes = R.drawable.white_red,
        modelName = Models.RED_T_SHIRT
    ),
    Shirt(
        nameRes = R.string.blue_t_shirt_name,
        painterRes = R.drawable.blue_red,
        modelName = Models.BLUE_T_SHIRT
    ),
    Shirt(
        nameRes = R.string.yellow_t_shirt_name,
        painterRes = R.drawable.yellow_red,
        modelName = Models.YELLOW_T_SHIRT
    ),
    Shirt(
        nameRes = R.string.dark_t_shirt_name,
        painterRes = R.drawable.red_black,
        modelName = Models.DARK_T_SHIRT
    ),
    Shirt(
        nameRes = R.string.white_t_shirt_name,
        painterRes = R.drawable.red_white,
        modelName = Models.WHITE_T_SHIRT
    ),
    Shirt(
        nameRes = R.string.gray_t_shirt_name,
        painterRes = R.drawable.grey_red,
        modelName = Models.GRAY_T_SHIRT
    ),
    Shirt(
        nameRes = R.string.green_t_shirt_name,
        painterRes = R.drawable.green_red,
        modelName = Models.GREEN_T_SHIRT
    )
)