package dev.yarobot.shirmaz.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.camera.model.Models
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.float3DPose
import dev.yarobot.shirmaz.platform.type
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector
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
import shirmaz.shirmazapp.generated.resources.Res

class CameraViewModel : ViewModel() {
    private val poseDetector = createPoseDetector(ShirmazPoseDetectorOptions.STREAM)

    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            currentModel = null
        )
    )

    val state = _state.onStart {
        loadModel(Models.sampleModel)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> intent.permissionsController.requestCamera()
            is CameraIntent.CheckCameraPermission ->
                intent.permissionsController.checkAndTryProvideCamera()

            is CameraIntent.OpenSettings -> intent.permissionsController.openSettings()
            is CameraIntent.OnImageCaptured -> detectPose(intent.image)
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

    private fun detectPose(image: PlatformImage) {
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                poseDetector.processImage(image){ poses, error ->
                    println("!!!!! start")
                    poses?.let {
                        it.forEach { pose ->
                            println("${pose.float3DPose()} ${pose.type}")
                        }
                    }
                    error?.let {
                        println(it)
                    }
                }
            }
        }
    }
}