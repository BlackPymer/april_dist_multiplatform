package dev.yarobot.shirmaz.camera

import com.google.android.filament.utils.Float3
import com.google.android.filament.utils.Mat4
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Quaternion
import com.google.android.filament.utils.rotation
import com.google.android.filament.utils.translation
import kotlin.math.*

class BoneController(private val modelViewer: ModelViewer) {

    fun setRotation(boneName: String, angle: Float3) {
        modelViewer.asset?.getFirstEntityByName(boneName)?.let { entity ->
            val defaultTransform = entity.getTransform()
            val transform = defaultTransform * rotation(angle)
            entity.setTransform(transform)
        }
        modelViewer.animator?.updateBoneMatrices()
    }

    fun setPosition(boneName: String, pos: Float3) {
        modelViewer.asset?.getFirstEntityByName(boneName)?.let { entity ->
            val defaultTransform = entity.getTransform()
            val transform = defaultTransform * translation(pos)
            entity.setTransform(transform)
        }
        modelViewer.animator?.updateBoneMatrices()
    }

    fun getRotation(boneName: String): Float3? =
        modelViewer.asset?.getFirstEntityByName(boneName)?.getTransform()?.rotation


    fun getPosition(boneName: String): Float3? =
        modelViewer.asset?.getFirstEntityByName(boneName)?.getTransform()?.translation


    fun quaternionToEuler(q: Quaternion): Float3 {
        val sqrOfY = q.y * q.y

        val t0 = +2.0 * (q.w * q.x + q.y * q.z)
        val t1 = +1.0 - 2.0 * (q.x * q.x + sqrOfY)
        val roll = atan2(t0, t1)

        var t2 = +2.0 * (q.w * q.y - q.z * q.x)
        t2 = if (t2 > 1.0) 1.0 else t2
        t2 = if (t2 < -1.0) -1.0 else t2
        val pitch = asin(t2)

        val t3 = +2.0 * (q.w * q.z + q.x * q.y)
        val t4 = +1.0 - 2.0 * (sqrOfY + q.z * q.z)
        val yaw = atan2(t3, t4)

        return Float3(roll.toFloat(), pitch.toFloat(), yaw.toFloat())
    }


    private fun Int.getTransform(): Mat4 {
        val tm = modelViewer.engine.transformManager
        val arr = FloatArray(16)
        tm.getTransform(tm.getInstance(this), arr)
        return Mat4.of(*arr)
    }

    private fun Int.setTransform(mat: Mat4) {
        val tm = modelViewer.engine.transformManager
        tm.setTransform(tm.getInstance(this), mat.toFloatArray())
    }
}