package dev.yarobot.shirmaz.camera

import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.rotation
import com.google.android.filament.utils.translation
import com.google.android.filament.utils.x

class BoneController(private val modelViewer: ModelViewer) {

    var String.rotation: Float3?
        get() = modelViewer.asset?.getFirstEntityByName(this)?.getTransform()?.rotation
        set(value) {
            modelViewer.asset?.getFirstEntityByName(this)?.let { entity ->
                val defaultTransform = entity.getTransform()
                val transform = defaultTransform * rotation(value ?: Float3())
                entity.setTransform(transform)
            }
            modelViewer.animator?.updateBoneMatrices()
        }

    var String.position: Float3?
        get() = modelViewer.asset?.getFirstEntityByName(this)?.getTransform()?.translation
        set(value) {
            require(value != null) { "Position must not be null." }

            val boneIndex = modelViewer.asset?.getFirstEntityByName(this)
                ?: throw IllegalArgumentException("Bone with name $this not found.")

            val transformManager = modelViewer.engine.transformManager
            val boneInstance = transformManager.getInstance(boneIndex)
            if (boneInstance == 0) throw IllegalArgumentException("Bone $this is not part of the TransformManager.")

            transformManager.setTransform(
                boneInstance,
                FloatArray(16).apply {
                    this[0] = 1f; this[5] = 1f; this[10] = 1f; this[15] = 1f
                    this[12] = value.x
                    this[13] = value.y
                    this[14] = value.z
                }
            )
        }




    private fun Int.getTransform(): Mat4 {
        val transformManager = modelViewer.engine.transformManager
        val arr = FloatArray(16)
        transformManager.getTransform(transformManager.getInstance(this), arr)
        return Mat4.of(*arr)
    }

    private fun Int.setTransform(mat: Mat4) {
        val transformManager = modelViewer.engine.transformManager
        transformManager.setTransform(transformManager.getInstance(this), mat.toFloatArray())
    }
}