package dev.yarobot.shirmaz.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.camera.model.CameraType
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

class CameraViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            currentShirt = null,
            currentModel = null,
            savingState = CameraSavingState.NotSaving,
            currentCamera = CameraType.FRONT,
            capturedPhoto = null,
            viewCreated = false,
            galleryPicture = null,
            appMode = AppMode.CameraMode
        )
    )

    val state = _state.asStateFlow()

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> requestCamera(intent.permissionsController)
            is CameraIntent.TakePicture -> takePicture()
            is CameraIntent.OpenGallery -> openGallery(intent.imageBitmap)
            is CameraIntent.ChooseShirt -> intent.shirt.chooseAsCurrent()
            is CameraIntent.BackToToolbar -> backToToolbar()
            is CameraIntent.SaveImage -> saveImage()
            is CameraIntent.ChangeCamera -> changeCamera()
            is CameraIntent.SetImage -> setImage(intent.imageBitmap)
            is CameraIntent.OnImageCreated -> onImageCreated()
            is CameraIntent.ViewCreated -> viewCreated()
            is CameraIntent.OnGalleryButton -> onGalleryButton()
        }
    }

    private fun setImage(imageBitmap: ImageBitmap) {
        _state.update {
            it.copy(capturedPhoto = imageBitmap)
        }
    }

    private fun backToToolbar() {
        _state.update {
            it.copy(savingState = CameraSavingState.NotSaving)
        }
    }

    private fun takePicture() {
        _state.update {
            it.copy(savingState = CameraSavingState.CreatingImage)
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

    private fun saveImage() {
        _state.update {
            it.copy(
                savingState = CameraSavingState.NotSaving,
                capturedPhoto = null,
                viewCreated = false
            )
        }
    }

    private fun onImageCreated() {
        _state.update {
            it.copy(savingState = CameraSavingState.Saving)
        }
    }

    private fun viewCreated() {
        if (_state.value.savingState == CameraSavingState.CreatingImage && !_state.value.viewCreated) {
            _state.update {
                it.copy(
                    viewCreated = true
                )
            }
        }
    }

    private fun openGallery(imageBitmap: ImageBitmap?) {
        _state.update {
            it.copy(
                galleryPicture = imageBitmap,
                capturedPhoto = imageBitmap
            )
        }

    }

    private fun onGalleryButton() {
        if (_state.value.appMode == AppMode.CameraMode) {
            _state.update {
                it.copy(
                    appMode = AppMode.GalleryMode
                )
            }
        } else {
            _state.update {
                it.copy(
                    appMode = AppMode.CameraMode,
                    galleryPicture = null,
                    capturedPhoto = null
                )
            }
        }
    }
}