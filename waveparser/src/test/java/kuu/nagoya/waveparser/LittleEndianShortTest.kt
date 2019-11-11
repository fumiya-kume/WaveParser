package kuu.nagoya.waveparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LittleEndianShortTest {
    @ParameterizedTest(name = "try convert to little Endian short: {0}")
    @ValueSource(shorts = shortArrayOf(0, 1, 10, 100, Short.MAX_VALUE))
    fun TryConvert(short: Short) {
        LittleEndianShort.of(short)
    }

    @ParameterizedTest(name = "try convert back to little Endian short: {0}")
    @ValueSource(shorts = shortArrayOf(0, 1, 10, 100, Short.MAX_VALUE))
    fun TryConvertBack(short: Short) {
        val result = LittleEndianShort.of(short)
        Assertions.assertEquals(short, result.readValue())
    }

    @Nested
    inner class checkLittleEndianShortValue {
        val source: Short = 1
        val littleShort = LittleEndianShort.of(source)
        val result = littleShort.value

        @Test
        fun littleEndianSize() = Assertions.assertEquals(2, result.size)

        @Test
        fun checkFistItem() = Assertions.assertEquals(1, result.elementAt(0))

        @Test
        fun checkSecondItem() = Assertions.assertEquals(0, result.elementAt(1))

    }
}