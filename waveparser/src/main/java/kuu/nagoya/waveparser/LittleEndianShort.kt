package kuu.nagoya.waveparser

import java.nio.ByteBuffer
import java.nio.ByteOrder

class LittleEndianShort private constructor(
    val value: ByteArray
) {
    fun readValue(): Short {
        return ByteBuffer.wrap(value)
            .order(ByteOrder.LITTLE_ENDIAN)
            .asShortBuffer()
            .get()
    }

    companion object {
        fun of(value: Short): LittleEndianShort {
            return LittleEndianShort(
                (0..1)
                    .map { it * 8 }
                    .map { value.toInt().shr(it).toByte() }
                    .toByteArray()
            )
        }
    }
}