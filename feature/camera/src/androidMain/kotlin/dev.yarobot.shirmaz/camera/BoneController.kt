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
            modelViewer.asset?.getFirstEntityByName(this)?.let { entity ->
                val defaultTransform = entity.getTransform()
                val transform = defaultTransform * translation(value ?: Float3())
                entity.setTransform(transform)
            }
            modelViewer.animator?.updateBoneMatrices()
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