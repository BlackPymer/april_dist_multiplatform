package dev.yarobot.shirmaz.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.platform.float3DPose
import dev.yarobot.shirmaz.platform.PlatformImage
import dev.yarobot.shirmaz.platform.type
import dev.yarobot.shirmaz.posedetection.ShirmazPoseDetectorOptions
import dev.yarobot.shirmaz.posedetection.createPoseDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraViewModel : ViewModel() {
    private val poseDetector = createPoseDetector(ShirmazPoseDetectorOptions.STREAM)

    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
        )
    )
    val state = _state.asStateFlow()

    fun onIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.RequestCamera -> intent.permissionsController.requestCamera()
            is CameraIntent.CheckCameraPermission -> intent.permissionsController
                .proceedCameraState()

            is CameraIntent.OnImageCaptured -> detectPose(intent.image)
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