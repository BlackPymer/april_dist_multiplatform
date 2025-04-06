package dev.yarobot.shirmaz.camera.model

data class Model3D(
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Model3D

        return bytes.contentEquals(other.bytes)
    }

    override fun hashCode(): Int {
        return bytes.contentHashCode()
    }
}