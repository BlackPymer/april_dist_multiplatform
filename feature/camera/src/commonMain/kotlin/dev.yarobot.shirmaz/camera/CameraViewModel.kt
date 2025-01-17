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

class CameraViewModel(shirts: Array<Shirt>) : MVIViewModel<CameraIntent, CameraScreenState>() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            isUnclothes = false,
            shirts = shirts,
            chosenShirt = 1
        )
    )
    override val state = _state.asStateFlow()

    override fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> intent.permissionsController.requestCamera()
            is CameraIntent.CheckCameraPermission -> intent.permissionsController
                .proceedCameraState()
            is CameraIntent.TakePicture -> {}
            is CameraIntent.OpenGallery -> {}
            is CameraIntent.Unclothes -> updateUnclothes()
            is CameraIntent.ChooseShirt -> intent.index.chooseShirt()
        }
    }

    private fun PermissionsController.requestCamera() {
        viewModelScope.launch {
            this@requestCamera.providePermission(Permission.CAMERA)
        }
        this.proceedCameraState()
    }

    private fun PermissionsController.proceedCameraState() =
        viewModelScope.launch {
            when (this@proceedCameraState.getPermissionState(Permission.CAMERA)) {
                PermissionState.Granted -> {
                    _state.update {
                        it.copy(cameraProvideState = CameraProvideState.Granted)
                    }
                }

                else -> _state.update { it.copy(cameraProvideState = CameraProvideState.NotGranted) }
            }
        }

    private fun updateUnclothes() {
        _state.update {
            it.copy(isUnclothes = !it.isUnclothes)
        }
    }

    private fun Int.chooseShirt() {
        _state.update{
            it.copy(chosenShirt = this@chooseShirt)
        }
    }
}