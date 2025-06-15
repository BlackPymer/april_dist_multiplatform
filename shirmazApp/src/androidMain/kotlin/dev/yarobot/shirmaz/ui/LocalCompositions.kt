package dev.yarobot.shirmaz.ui

import androidx.compose.runtime.compositionLocalOf
import dev.icerock.moko.permissions.PermissionsController

val LocalPermissionsController = compositionLocalOf<PermissionsController> {
    error("No Permissions Controller provided")
}

val LocalShirmazColors = compositionLocalOf<ShirmazColors> {
    error("No provided colors")
}

val LocalShirmazDimension = compositionLocalOf<ShirmazDimension> {
    error("No provided dimensions")
}
