package dev.yarobot.shirmaz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.android.filament.utils.Utils
import dev.yarobot.shirmaz.App

class ShirmazActivity : ComponentActivity() {
    companion object {
        init {
            Utils.init()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}