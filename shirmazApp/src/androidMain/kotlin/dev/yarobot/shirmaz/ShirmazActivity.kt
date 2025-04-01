package dev.yarobot.shirmaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.filament.utils.Utils
import dev.yarobot.shirmaz.camera.initAppContext

class ShirmazActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppContext(this)
        installSplashScreen()
        setContent {
            App()
        }
    }
}