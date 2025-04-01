package dev.yarobot.shirmaz.camera

sealed interface CameraSavingState {
    data object NotSaving: CameraSavingState
    data object CreatingImage: CameraSavingState
    data object Saving: CameraSavingState
}