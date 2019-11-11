package kuu.nagoya.waveparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class parseWaveFile {
    @Nested
    inner class tryLoadWaveFile() {
        val classLoader = this.javaClass.classLoader ?: Assertions.fail()

        val filePath = classLoader.getResource("cat.wav")
        val model = WaveModel.loadFromFile(filePath.path)

        fun ByteArray.convertToString(): String {
            return this.map { it.toInt().toChar() }.fold("") { c1, c2 -> c1 + c2 }
        }

        @Test
        @DisplayName("chunk Id is \"RIFF\" ")
        fun checkChunkId() {
            val chunkId = model.chunkId.id.convertToString()
            Assertions.assertEquals("RIFF", chunkId)
        }

        @Test
        @DisplayName("format is : \"WAVE\"")
        fun checkFormat() {
            val format = model.format.format.convertToString()
            Assertions.assertEquals("WAVE", format)

        }

        @Test
        @DisplayName("sub chunkid is \"fmt \"")
        fun checkSubChunkId() {
            val subChunk1ID = model.subChunkId.id
            Assertions.assertEquals(
                "fmt ",
                subChunk1ID.convertToString()
            )
        }

        @Test
        @DisplayName("audio format is [1,0]")
        fun checkAudioFormat() {
            val audioFormat = model.audioFormat.formatId
            Assertions.assertEquals(2, audioFormat.size)
            Assertions.assertEquals(0, audioFormat.elementAt(0))
            Assertions.assertEquals(1, audioFormat.elementAt(1))
        }

        @Test
        @DisplayName("number of channel: 2")
        fun checkNumberOfChannel() {
            val numChannels = model.numChannels.number.readValue()
            Assertions.assertEquals(2, numChannels)

        }

        @Test
        @DisplayName("sampling rate is : 48000")
        fun checkSamplingRate() {
            val sampleRate = model.samplingRate.rate.readValue()
            Assertions.assertEquals(48000, sampleRate)
        }
    }
}
