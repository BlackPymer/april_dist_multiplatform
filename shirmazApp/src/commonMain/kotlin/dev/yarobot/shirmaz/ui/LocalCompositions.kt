package dev.yarobot.shirmaz.ui

import androidx.compose.runtime.compositionLocalOf
import dev.icerock.moko.permissions.PermissionsController

val LocalPermissionsController = compositionLocalOf<PermissionsController> {
    error("No Permissions Controller provided")
}
