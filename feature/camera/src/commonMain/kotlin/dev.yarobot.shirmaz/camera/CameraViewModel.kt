package dev.yarobot.shirmaz.camera

import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.camera.model.Models
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.core.language.MVIViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import shirmaz.feature.camera.generated.resources.Res


class CameraViewModel(shirts: Array<Shirt>) : MVIViewModel<CameraIntent, CameraScreenState>() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            isUnclothes = false,
            shirts = shirts,
            chosenShirt = 1
            currentModel = null

        )
    )
    override val state = _state.onStart {
        loadModel(Models.sampleModel)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

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