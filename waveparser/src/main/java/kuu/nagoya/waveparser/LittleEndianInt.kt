package kuu.nagoya.waveparser

import java.nio.ByteBuffer
import java.nio.ByteOrder

class LittleEndianInt private constructor(
    private val value: ByteArray
) {

    fun readValue(): Int {
        return ByteBuffer.wrap(value)
            .order(ByteOrder.LITTLE_ENDIAN)
            .asIntBuffer()
            .get()
    }

    companion object {
        fun of(value: Int): LittleEndianInt {
            return LittleEndianInt(
                (0..3)
                    .map { it * 8 }
                    .map { value.shr(it).toByte() }
                    .toByteArray()
            )
        }
    }
}