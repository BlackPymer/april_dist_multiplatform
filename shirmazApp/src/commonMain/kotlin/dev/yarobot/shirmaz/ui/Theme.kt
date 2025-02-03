package dev.yarobot.shirmaz.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.yarobot.shirmaz.core.compose.base.LocalPermissionsController
import dev.yarobot.shirmaz.core.compose.base.LocalShirmazColors
import dev.yarobot.shirmaz.core.compose.base.LocalShirmazDimension
import dev.yarobot.shirmaz.core.compose.base.shirmazColors
import dev.yarobot.shirmaz.core.compose.base.shirmazDimension


@Composable
fun ShirmazTheme(content: @Composable () -> Unit) {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController = remember(factory) {
        factory.createPermissionsController()
    }
    BindEffect(controller)
    CompositionLocalProvider(
        LocalPermissionsController provides controller,
        LocalShirmazColors provides shirmazColors,
        LocalShirmazDimension provides shirmazDimension,
    ) {
        content()
    }
}
object ShirmazTheme{
    val colors
        @Composable
        get() = LocalShirmazColors.current
    val dimension
        @Composable
        get() = LocalShirmazDimension.current
}