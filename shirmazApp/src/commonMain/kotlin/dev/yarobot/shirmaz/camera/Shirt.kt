package dev.yarobot.shirmaz.camera

import dev.yarobot.shirmaz.camera.model.Models
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.clothes
import shirmaz.shirmazapp.generated.resources.shirt1_name

data class Shirt(
    val nameRes: StringResource,
    val painterRes: DrawableResource,
    val modelName: String
)

val shirts = listOf(
    Shirt(
        nameRes = Res.string.shirt1_name,
        painterRes = Res.drawable.clothes,
        modelName = Models.sampleModel
    ),
)