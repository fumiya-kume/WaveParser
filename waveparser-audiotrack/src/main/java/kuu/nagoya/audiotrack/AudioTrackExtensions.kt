package kuu.nagoya.audiotrack

import android.media.AudioRecord
import kuu.nagoya.waveparser.BitPerSample
import kuu.nagoya.waveparser.NumChannels
import kuu.nagoya.waveparser.SamplingRate
import kuu.nagoya.waveparser.WaveModel
import java.nio.ByteBuffer
import kotlin.math.abs


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
    val currentAudioData: MutableList<Short> = mutableListOf()
    private var maxAmplitude = 0

    private val bitPerSample =
        BitPerSample.of(
            when (audioFormat) {
                AudioFormat.ENCODING_PCM_16BIT -> BitPerSample.sixteenBit
                AudioFormat.ENCODING_PCM_8BIT -> BitPerSample.eightBit
                else -> BitPerSample.sixteenBit
            }
        )

    init {
        this.setRecordPositionUpdateListener(object : OnRecordPositionUpdateListener {
            override fun onMarkerReached(recorder: AudioRecord?) {
                if (recorder == null) {
                    return
                }
                val data = ByteBuffer.allocate(bufferSizeInBytes)
                recorder.read(data, bufferSizeInBytes)
                currentAudioData.clear()
                currentAudioData.addAll(data.asShortBuffer().array().toList())

                maxAmplitude = currentAudioData
                    .map { it.toInt() }
                    .map { abs(it) }
                    .max() ?: 0

                audioData += data.asShortBuffer().array().toList()
            }

            override fun onPeriodicNotification(recorder: AudioRecord?) {

            }

        })
    }

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