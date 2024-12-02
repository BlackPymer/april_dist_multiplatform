package dev.yarobot.shirmaz.core.compose.base

import androidx.compose.runtime.compositionLocalOf
import dev.icerock.moko.permissions.PermissionsController

val LocalPermissionsController = compositionLocalOf<PermissionsController> {
    error("No Permissions Controller provided")
}
