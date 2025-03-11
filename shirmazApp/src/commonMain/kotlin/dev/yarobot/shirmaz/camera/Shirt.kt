package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.Models
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import shirmaz.shirmazapp.generated.resources.BLUERED
import shirmaz.shirmazapp.generated.resources.GREENRED
import shirmaz.shirmazapp.generated.resources.GREYRAD
import shirmaz.shirmazapp.generated.resources.REDWHITE
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.WHITERED
import shirmaz.shirmazapp.generated.resources.YELLOWRED
import shirmaz.shirmazapp.generated.resources.blue_t_shirt_name
import shirmaz.shirmazapp.generated.resources.dark_t_shirt_name
import shirmaz.shirmazapp.generated.resources.gray_t_shirt_name
import shirmaz.shirmazapp.generated.resources.green_t_shirt_name
import shirmaz.shirmazapp.generated.resources.red_t_shirt_name
import shirmaz.shirmazapp.generated.resources.white_t_shirt_name
import shirmaz.shirmazapp.generated.resources.yellow_t_shirt_name

data class Shirt(
    val nameRes: StringResource,
    val painterRes: DrawableResource,
    val modelName: String
)

val shirts = listOf(
    Shirt(
        nameRes = Res.string.red_t_shirt_name,
        painterRes = Res.drawable.WHITERED,
        modelName = Models.RED_T_SHIRT
    ),
    Shirt(
        nameRes = Res.string.blue_t_shirt_name,
        painterRes = Res.drawable.BLUERED,
        modelName = Models.BLUE_T_SHIRT
    ),
    Shirt(
        nameRes = Res.string.yellow_t_shirt_name,
        painterRes = Res.drawable.YELLOWRED,
        modelName = Models.YELLOW_T_SHIRT
    ),
//    Shirt(
//        nameRes = Res.string.dark_t_shirt_name,
//        painterRes = Res.drawable.REDBLACK,
//        modelName = Models.DARK_T_SHIRT
//    ),
    Shirt(
        nameRes = Res.string.white_t_shirt_name,
        painterRes = Res.drawable.REDWHITE,
        modelName = Models.WHITE_T_SHIRT
    ),
    Shirt(
        nameRes = Res.string.gray_t_shirt_name,
        painterRes = Res.drawable.GREYRAD,
        modelName = Models.GRAY_T_SHIRT
    ),
    Shirt(
        nameRes = Res.string.green_t_shirt_name,
        painterRes = Res.drawable.GREENRED,
        modelName = Models.GREEN_T_SHIRT
    )
)