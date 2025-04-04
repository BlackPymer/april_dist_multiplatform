package dev.yarobot.shirmaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.yarobot.shirmaz.camera.CameraScreen
import dev.yarobot.shirmaz.camera.initAppContext
import dev.yarobot.shirmaz.ui.ShirmazTheme

class ShirmazActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAppContext(this)
        installSplashScreen()
        setContent {
            ShirmazTheme {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CameraScreen()
                }
            }
        }
    }
}