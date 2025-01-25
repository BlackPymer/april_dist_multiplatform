package dev.yarobot.shirmaz.camera

import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import shirmaz.feature.camera.generated.resources.Res

class CameraViewModel : MVIViewModel<CameraIntent, CameraScreenState>() {
    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            currentModel = null
        )
    )

    override val state = _state.asStateFlow()

    override fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> intent.permissionsController.requestCamera()
            is CameraIntent.CheckCameraPermission ->
                intent.permissionsController.checkAndTryProvideCamera()

            is CameraIntent.OpenSettings -> intent.permissionsController.openSettings()
        }
    }

    private fun PermissionsController.openSettings() {
        this@openSettings.openAppSettings()
    }

    private fun PermissionsController.requestCamera() {
        viewModelScope.launch {
            try {
                this@requestCamera.providePermission(Permission.CAMERA)
                proceedCameraState()
            } catch (e: Exception) {
                    openSettings()
            }
        }
    }

    private fun PermissionsController.checkAndTryProvideCamera() {
        viewModelScope.launch {
            this@checkAndTryProvideCamera.proceedCameraState()
        }
    }

    private suspend fun PermissionsController.proceedCameraState() =
        when (this@proceedCameraState.getPermissionState(Permission.CAMERA)) {
            PermissionState.Granted -> {
                _state.update {
                    it.copy(cameraProvideState = CameraProvideState.Granted)
                }
            }
            else -> {}
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