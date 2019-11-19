package kuu.nagoya.audiotrack

import android.media.AudioRecord
import kuu.nagoya.waveparser.BitPerSample
import kuu.nagoya.waveparser.NumChannels
import kuu.nagoya.waveparser.SamplingRate
import kuu.nagoya.waveparser.WaveModel


class AudioRecordWithWave(
    private val filePath: String,
    val audioSource: AudioSource = AudioSource.DEFAULT,
    val samplingRate: Int = 44100,
    val channelConfig: ChannelConfig = ChannelConfig.CHANNEL_IN_MONO,
    val audioFormat: AudioFormat = AudioFormat.ENCODING_PCM_16BIT,
    val bufferSizeInBytes: Int = getMinBufferSize(
        samplingRate, channelConfig.value,
        audioFormat.value
    ) / 2
) : AudioRecord(
    audioSource.value,
    samplingRate,
    channelConfig.value,
    audioFormat.value,
    bufferSizeInBytes
) {
    private val audioData: MutableList<Short> = mutableListOf()
    private val bitPerSample =
        BitPerSample.of(
            when (audioFormat) {
                AudioFormat.ENCODING_PCM_16BIT -> BitPerSample.sixteenBit
                AudioFormat.ENCODING_PCM_8BIT -> BitPerSample.eightBit
                else -> BitPerSample.sixteenBit
            }
        )

    override fun startRecording() {
        audioData.clear()

        super.startRecording()
    }

    override fun stop() {
        super.stop()

        WaveModel.storeToFile(
            filePath,
            audioData,
            NumChannels.of(channelConfig.value),
            bitPerSample,
            SamplingRate.of(samplingRate),
            true
        )
    }
}