package dev.yarobot.shirmaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ShirmazActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}