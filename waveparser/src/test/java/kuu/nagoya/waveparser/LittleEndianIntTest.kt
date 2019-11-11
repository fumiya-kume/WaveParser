package kuu.nagoya.waveparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LittleEndianIntTest {

    @ParameterizedTest(name = "try transformation: {0}")
    @ValueSource(ints = intArrayOf(0, 1, 100, 10000, Int.MAX_VALUE))
    fun TryTransformation(int: Int) {
        val littleEndianInt = LittleEndianInt.of(int)
    }

    @ParameterizedTest(name = "try transformationBack: {0}")
    @ValueSource(ints = intArrayOf(0, 1, 100, 10000, Int.MAX_VALUE))
    fun TryTransformationBack(int: Int) {
        val littleEndianInt = LittleEndianInt.of(int)
        Assertions.assertEquals(int, littleEndianInt.readValue())
    }
}