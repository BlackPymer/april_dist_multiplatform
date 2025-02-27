package dev.yarobot.shirmaz.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.camera.model.CameraType
import dev.yarobot.shirmaz.camera.model.Models
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import shirmaz.shirmazapp.generated.resources.Res
import shirmaz.shirmazapp.generated.resources.clothes
import shirmaz.shirmazapp.generated.resources.dark_t_shirt_name
import shirmaz.shirmazapp.generated.resources.mia
import shirmaz.shirmazapp.generated.resources.mia_name
import shirmaz.shirmazapp.generated.resources.jacket_name
import shirmaz.shirmazapp.generated.resources.t_shirt_name

class CameraViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            shirts = listOf(
                Shirt(
                    nameRes = (Res.string.t_shirt_name),
                    painterRes = (Res.drawable.clothes),
                    modelName = Models.T_SHIRT
               ),
                Shirt(
                    nameRes = (Res.string.mia_name),
                    painterRes = (Res.drawable.mia),
                    modelName = Models.MAI_CHARACTER
                ),
                Shirt(
                    nameRes = (Res.string.jacket_name),
                    painterRes = (Res.drawable.clothes),
                    modelName = Models.LEATHER_JACKET
                ),
                Shirt(
                    nameRes = (Res.string.dark_t_shirt_name),
                    painterRes = (Res.drawable.clothes),
                    modelName = Models.DARK_T_SHIRT
                )
            ),
            currentShirt = null,
            currentModel = null,
            saving = false,
            currentCamera = CameraType.BACK,
        )
    )

    val state = _state.asStateFlow()

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> requestCamera(intent.permissionsController)
            is CameraIntent.TakePicture -> takePicture()
            is CameraIntent.OpenGallery -> {}
            is CameraIntent.ChooseShirt -> intent.shirt.chooseAsCurrent()
            is CameraIntent.BackToToolbar -> backToToolbar()
            is CameraIntent.SaveImage -> {}
            CameraIntent.ChangeCamera -> changeCamera()
        }
    }

    private fun backToToolbar() {
        _state.update {
            it.copy(saving = false)
        }
    }

    private fun takePicture() {
        _state.update {
            it.copy(saving = true)
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
            it.copy(
                currentShirt = this,
                currentModel = null
            )
        }
        this?.modelName?.let {
            loadModel(this.modelName)
        }
    }


    @OptIn(ExperimentalResourceApi::class)
    private fun loadModel(modelName: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _state.update {
                it.copy(
                    currentModel = ThreeDModel(
                        Res.readBytes("files/$modelName")
                    )
                )
            }
        }
    }
}