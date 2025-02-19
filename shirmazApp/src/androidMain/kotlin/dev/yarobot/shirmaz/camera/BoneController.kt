package dev.yarobot.shirmaz.camera

import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.rotation
import java.nio.FloatBuffer

class BoneController(private val modelViewer: ModelViewer) {
    private val renderableManager = modelViewer.engine.renderableManager
    private val transformManager = modelViewer.engine.transformManager
    private val assets = modelViewer.asset

    var String.rotation: Float3?
        get() = assets?.getFirstEntityByName(this)?.getTransform()?.rotation
        set(value) {
            assets?.getFirstEntityByName(this)?.let { entity ->
                val defaultTransform = entity.getTransform()
                val transform = defaultTransform * rotation(value ?: Float3())
                entity.setTransform(transform)
            }
            modelViewer.animator?.updateBoneMatrices()
        }

    var String.position: Float3?
        get() = assets?.getFirstEntityByName(this)?.getTransform()?.translation
        set(value) {
            value?.let {
                val boneIndex = assets?.getFirstEntityByName(this) ?: return@let

                val boneInstance = transformManager.getInstance(boneIndex)
                if (boneInstance == 0) error("Bone $this is not part of the TransformManager.")
                val currentTransform = FloatArray(16)
                transformManager.getTransform(boneInstance, currentTransform)

                currentTransform[12] = value.x
                currentTransform[13] = value.y
                currentTransform[14] = value.z

                transformManager.setTransform(boneInstance, currentTransform)
            }
            modelViewer.animator?.updateBoneMatrices()
        }


    private fun Int.getTransform(): Mat4 {
        val arr = FloatArray(16)
        transformManager.getTransform(transformManager.getInstance(this), arr)
        return Mat4.of(*arr)
    }

    private fun Int.setTransform(mat: Mat4) {
        renderableManager.setBonesAsQuaternions(
            transformManager.getInstance(this),
            FloatBuffer.wrap(mat.toFloatArray()), 1, this
        )
    }
}
