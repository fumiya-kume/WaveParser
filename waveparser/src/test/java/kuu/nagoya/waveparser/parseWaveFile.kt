package kuu.nagoya.waveparser

import org.junit.jupiter.api.Assertions
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
    inner class CheckData {
        val model = WaveParse.loadWaveFromFile(File(loadSampleResource("cat.wav").path))
        val outputPath = loadSampleResource("cat.wav").path.replace("cat", "dog")
        val dogModel:Wave
        init {
            WaveParse.storeWaveFile(model, File(outputPath))
            dogModel = WaveParse.loadWaveFromFile(File(outputPath))
        }

        @Test
        fun checkChunkId()
        {
            Assertions.assertEquals(model.chunkId,dogModel.chunkId)
        }

        @Test
        fun checkChunkSize(){
            Assertions.assertEquals(model.chunkSize,dogModel.chunkSize)
        }

        @Test
        fun checkFormat(){
            Assertions.assertEquals(model.format,dogModel.format)
        }

        @Test
        fun checkSubChunkId(){
            Assertions.assertEquals(model.subChunk1Id,dogModel.subChunk1Id)
        }

        @Test
        fun checkSubChunkSize(){
            Assertions.assertEquals(model.subChunk1Size,dogModel.subChunk1Size)
        }

        @Test
        fun checkAudioFormat(){
            Assertions.assertEquals(model.audioFormat,dogModel.audioFormat)
        }

        @Test
        fun checkNumChannels(){
            Assertions.assertEquals(model.numChannels,dogModel.numChannels)
        }

        @Test
        fun checkSampleRate(){
            Assertions.assertEquals(model.sampleRate,dogModel.sampleRate)
        }
//        val byteRate: Int,
//        val blockAlign: Int,
//        val bitPerSample: Int,
//        val subCHunk2Id: Int,
//        val subChunk2Size: Int,

       @Test
       fun checkData(){
           Assertions.assertEquals(model.data.size,dogModel.data.size)
           model.data.forEachIndexed { index, sh -> Assertions.assertEquals(sh,dogModel.data.elementAt(index)) }
       }
    }
}
