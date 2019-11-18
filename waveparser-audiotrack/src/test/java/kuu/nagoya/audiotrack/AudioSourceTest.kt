package kuu.nagoya.audiotrack

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

internal class AudioSourceTest {
    companion object {
        @JvmStatic
        fun readAudioSourceValueData() = listOf(
            arrayOf(0, AudioSource.DEFAULT),
            arrayOf(1, AudioSource.MIC)
        )
    }

    @Test
    fun makeAudioSource() {
        val audioSource = AudioSource.DEFAULT
    }

    @ParameterizedTest
    @MethodSource("readAudioSourceValueData")
    fun readAudioSourceValue(actual: Int, audioSource: AudioSource) {
        Assertions.assertEquals(audioSource.value, actual)
    }
}