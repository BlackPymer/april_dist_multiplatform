package dev.yarobot.shirmaz.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

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
        MaterialTheme(typography = shirmazTypography) {
            content()
        }
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