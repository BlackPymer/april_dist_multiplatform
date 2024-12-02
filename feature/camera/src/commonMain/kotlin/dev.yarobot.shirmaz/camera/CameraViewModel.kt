package dev.yarobot.shirmaz.camera

import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.core.language.MVIViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel() : MVIViewModel<CameraIntent, CameraScreenState>() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted
        )
    )
    override val state = _state.asStateFlow()

    override fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> {
                intent.permissionsController.provide(Permission.CAMERA)
                intent.permissionsController.proceedState(Permission.CAMERA)
            }
        }
    }

    private fun PermissionsController.proceedState(permission: Permission) =
        viewModelScope.launch {
            when (this@proceedState.getPermissionState(permission)) {
                PermissionState.Granted -> {
                    _state.update {
                        it.copy(cameraProvideState = CameraProvideState.Granted)
                    }
                }

                else -> _state.update { it.copy(cameraProvideState = CameraProvideState.NotGranted) }
            }
        }


    private fun PermissionsController.provide(permission: Permission) =
        viewModelScope.launch {
            this@provide.providePermission(permission)
        }
}