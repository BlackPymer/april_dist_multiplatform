package dev.yarobot.shirmaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.yarobot.shirmaz.camera.initAppContext
import dev.yarobot.shirmaz.platform.ActualContext
import dev.yarobot.shirmaz.ui.LocalActualContext

class ShirmazActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppContext(this)
        installSplashScreen()
        setContent {
            CompositionLocalProvider(
                LocalActualContext provides ActualContext(this)
            ) {
                App()
            }
        }
    }
}