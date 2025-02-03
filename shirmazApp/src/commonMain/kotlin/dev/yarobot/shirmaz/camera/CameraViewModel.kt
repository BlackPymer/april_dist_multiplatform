package dev.yarobot.shirmaz.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.yarobot.shirmaz.camera.model.Models
import dev.yarobot.shirmaz.camera.model.ThreeDModel
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
import shirmaz.feature.camera.generated.resources.clothes
import shirmaz.feature.camera.generated.resources.null_shirt
import shirmaz.feature.camera.generated.resources.shirt1_name
import shirmaz.feature.camera.generated.resources.shirt2_name
import shirmaz.feature.camera.generated.resources.shirt3_name
import shirmaz.feature.camera.generated.resources.unclothes
import shirmaz.feature.camera.generated.resources.unclothes_no_text
import shirmaz.shirmazapp.generated.resources.Res

class CameraViewModel : ViewModel() {
    private val poseDetector = createPoseDetector(ShirmazPoseDetectorOptions.STREAM)

    private val _state = MutableStateFlow(
        CameraScreenState(
            cameraProvideState = CameraProvideState.NotGranted,
            shirts = listOf(
                Shirt(
                    nameRes = (Res.string.null_shirt),
                    painterRes = (Res.drawable.unclothes_no_text),
                    modelName = null
                ),
                Shirt(
                    nameRes = (Res.string.shirt1_name),
                    painterRes = (Res.drawable.clothes),
                    modelName = Models.sampleModel
                ),
                Shirt(
                    nameRes = (Res.string.shirt2_name),
                    painterRes = (Res.drawable.clothes),
                    modelName = Models.sampleModel
                ),
                Shirt(
                    nameRes = (Res.string.shirt3_name),
                    painterRes = (Res.drawable.clothes),
                    modelName = Models.sampleModel
                )
            ),
            currentShirt = null,
            currentModel = null,
            isCarouselVisible = false,
            saving = false
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
            is CameraIntent.CheckCameraPermission -> intent.permissionsController
                .proceedCameraState()

            is CameraIntent.OnImageCaptured -> detectPose(intent.image)

            is CameraIntent.TakePicture -> takePicture()
            is CameraIntent.OpenGallery -> {}
            is CameraIntent.ChangeCorouselVisability -> changeCorouselVisability()
            is CameraIntent.ChooseShirt -> intent.shirt.chooseAsCurrent()
            is CameraIntent.BackToToolbar -> backToToolbar()
            is CameraIntent.SaveImage -> {}
        }
    }

    private fun backToToolbar() {
        _state.update {
            it.copy(saving = false)
        }
    }

    private fun takePicture() {
        _state.update {
            it.copy(saving = true, isCarouselVisible = true)
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

    private fun changeCorouselVisability() {
        _state.update {
            it.copy(isCarouselVisible = !it.isCarouselVisible)
        }
    }

    private fun Shirt.chooseAsCurrent() {
        _state.update {
            it.copy(currentShirt = this)
        }
        loadModel()
    }


    @OptIn(ExperimentalResourceApi::class)
    private fun loadModel() = viewModelScope.launch {

        withContext(Dispatchers.IO) {
            if (_state.value.currentShirt?.modelName == null) {
                _state.update {
                    it.copy(
                        currentModel = null
                    )
                }
            }
            _state.value.currentShirt?.modelName?.let { name ->
                _state.update {
                    it.copy(
                        currentModel = ThreeDModel(
                            Res.readBytes("files/${name}")
                        )
                    )
                }
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