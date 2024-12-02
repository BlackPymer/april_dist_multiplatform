package dev.yarobot.shirmaz.core.language

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class MVIViewModel<I: Intent, S: ScreenState>: ViewModel(){
    abstract val state: StateFlow<S>
    abstract fun onIntent(intent: I)
}