package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.camera.model.CameraType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            currentShirt = null,
            isSaving = false,
            currentCamera = CameraType.FRONT,
            staticImage = null,
            viewCreated = false,
            appMode = AppMode.CameraMode
        )
    )

    val state = _state.asStateFlow()

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> requestCamera(intent.permissionsController)
            is CameraIntent.TakePicture -> takePicture()
            is CameraIntent.ChooseShirt -> intent.shirt.chooseAsCurrent()
            is CameraIntent.BackToCamera -> backToCamera()
            is CameraIntent.SaveImage -> saveImage()
            is CameraIntent.ChangeCamera -> changeCamera()
            is CameraIntent.SetImage -> setImage(intent.imageBitmap)
            is CameraIntent.OnImageCreated -> onImageCreated()
            is CameraIntent.ViewCreated -> viewCreated()
        }
    }

    private fun setImage(imageBitmap: ImageBitmap) {
        _state.update {
            it.copy(
                appMode = AppMode.StaticImage,
                isSaving = true,
                staticImage = imageBitmap
            )
        }
    }

    private fun backToCamera() {
        _state.update {
            it.copy(
                isSaving = false,
                staticImage = null,
                currentShirt = null,
                appMode = AppMode.CameraMode
            )
        }
    }

    private fun takePicture() {
        _state.update {
            it.copy(isSaving = true)
        }
    }

    private fun changeCamera() {
        val currentCamera = if (state.value.currentCamera == CameraType.FRONT)
            CameraType.BACK else CameraType.FRONT
        _state.update {
            it.copy(currentCamera = currentCamera)
        }
    }

    private fun requestCamera(controller: PermissionsController) {
        viewModelScope.launch {
            kotlin.runCatching {
                controller.providePermission(Permission.CAMERA)
            }.onSuccess {
                proceedCameraState(controller)
            }.onFailure {
                proceedCameraState(controller)
            }
        }
    }

    private suspend fun proceedCameraState(controller: PermissionsController) =
        when (controller.getPermissionState(Permission.CAMERA)) {
            PermissionState.Granted -> {
                _state.update {
                    it.copy(cameraProvideState = CameraProvideState.Granted)
                }
            }

            PermissionState.DeniedAlways -> controller.openAppSettings()
            PermissionState.Denied -> controller.openAppSettings()
            else -> {
                _state.update {
                    it.copy(cameraProvideState = CameraProvideState.NotGranted)
                }
            }
        }

    private fun Shirt?.chooseAsCurrent() {
        _state.update {
            it.copy(currentShirt = this)
        }
    }


    private fun saveImage() {
        _state.update {
            it.copy(
                appMode = AppMode.CameraMode,
                isSaving = false,
                staticImage = null,
                viewCreated = false
            )
        }
    }

    private fun onImageCreated() {
        _state.update {
            it.copy(isSaving = true)
        }
    }

    private fun viewCreated() {
        if (_state.value.isSaving && !_state.value.viewCreated) {
            _state.update {
                it.copy(viewCreated = true)
            }
        }
    }
}