
import dev.yarobot.shirmaz.camera.model.ThreeDModel
import dev.yarobot.shirmaz.core.language.ScreenState

data class CameraScreenState(
    val cameraProvideState: CameraProvideState,
    val currentModel: ThreeDModel?
): ScreenState