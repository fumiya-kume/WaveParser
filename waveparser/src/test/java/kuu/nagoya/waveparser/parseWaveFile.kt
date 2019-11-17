package kuu.nagoya.waveparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import java.net.URL

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class parseWaveFile {

    private fun loadSampleResource(sampleName: String): URL {
        val classLoader = this.javaClass.classLoader ?: Assertions.fail()
        return classLoader.getResource(sampleName)!!
    }

    private fun ByteArray.convertToString(): String {
        return this.map { it.toInt().toChar() }.fold("") { c1, c2 -> c1 + c2 }
    }

    @Nested
    inner class tryLoadWaveFile() {

        private val filePath = loadSampleResource("cat.wav")
        private val model = WaveModel.loadFromFile(filePath.path)

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

        @Test
        @DisplayName("bit per sample is : 16")
        fun checkBitPerSample() {
            val bitPerSample = model.bitPerSample.value.readValue()
            Assertions.assertEquals(16, bitPerSample)
        }

        @Test
        @DisplayName("audio data is not empty")
        fun checkAudioData() {
            val audioData = model.data
            Assertions.assertNotEquals(0, audioData.size)
        }
    }

    @Nested
    inner class tryStoreWaveFile {

        @Test
        fun storeWaveFile() {
            val waveModel = WaveModel.loadFromFile(loadSampleResource("cat.wav").path)
            val outputFilePath = loadSampleResource("cat.wav").path.replace("cat", "dog")
            val file = File(outputFilePath)
            if(file.exists()){
                file.delete()
            }

            val dataSize = waveModel.data.size
            Assertions.assertNotEquals(
                0,
                dataSize
            )

            WaveModel.storeToFile(
                outputFilePath,
                waveModel.data,
                waveModel.numChannels,
                waveModel.byteRate,
                waveModel.samplingRate
            )

            val outputFileInfo = WaveModel.loadFromFile(outputFilePath)


            Assertions.assertArrayEquals(waveModel.chunkId.id, outputFileInfo.chunkId.id)
            Assertions.assertArrayEquals(
                waveModel.chunkSize.size.value,
                outputFileInfo.chunkSize.size.value
            )
            Assertions.assertArrayEquals(waveModel.format.format, outputFileInfo.format.format)
            Assertions.assertArrayEquals(waveModel.subChunkId.id, outputFileInfo.subChunkId.id)

            Assertions.assertEquals(
                waveModel.bitPerSample.value.readValue(),
                outputFileInfo.bitPerSample.value.readValue()
            )

            Assertions.assertEquals(
                waveModel.subChunk2Size.size.readValue(),
                outputFileInfo.subChunk2Size.size.readValue()
            )
            Assertions.assertArrayEquals(
                waveModel.audioFormat.formatId,
                outputFileInfo.audioFormat.formatId
            )
            Assertions.assertEquals(
                waveModel.numChannels.number.readValue(),
                outputFileInfo.numChannels.number.readValue()
            )
            Assertions.assertEquals(
                waveModel.byteRate.value.readValue(),
                outputFileInfo.byteRate.value.readValue()
            )

            Assertions.assertEquals(
                waveModel.blockSize.size.readValue(),
                outputFileInfo.blockSize.size.readValue()
            )

            val outputFile = File(outputFilePath)
            outputFile.delete()
        }
    }
}
