package dev.yarobot.shirmaz.desktopapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.yarobot.shirmaz.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Shirmaz",
    ) {
        App()
    }
}