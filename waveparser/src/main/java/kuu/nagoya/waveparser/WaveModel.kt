package kuu.nagoya.waveparser

import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class WaveModel(
    val data: List<Short>,

    val chunkId: ChunkId = ChunkId.default(),
    val chunkSize: ChunkSize = ChunkSize.default(),
    val format: Format = Format.default(),
    val subChunkId: SubChunkId = SubChunkId.default(),
    val formatSize: FormatSize = FormatSize.default(),
    // TODO, Welcome to contribute, you can add any foramt implementation
    val audioFormat: AudioFormat = AudioFormat.default(),
    val numChannels: NumChannels = NumChannels.of(ChannelNumber.Mono),
    val samplingRate: SamplingRate = SamplingRate.default(),
    val bitPerSample: BitPerSample = BitPerSample.of(BitPerSample.sixteenBit),
    val bytePerSeccond: BytePerSecond =
        BytePerSecond.of(
            samplingRate,
            bitPerSample,
            numChannels
        ),
    val blockSize: BlockSize =
        BlockSize.of(
            numChannels,
            bitPerSample
        ),
    val subChunk2Id: SubChunk2Id = SubChunk2Id.default(),
    val subChunk2Size: SubChunk2Size = SubChunk2Size.of(
        data.size,
        numChannels,
        bitPerSample
    )
) {
    companion object {
        fun loadFromFile(
            filePath: String
        ): WaveModel {
            val file = File(filePath)
            if (!file.exists()) {
                throw FileNotFoundException("File not found at $filePath")
            }
            val fileByteList = file
                .readBytes()

            fun RandomAccessFile.readString(count: Int): String {
                val maxCount = count - 1
                return (0..maxCount)
                    .mapIndexed { index, _ -> this.readByte() }
                    .fold("") { c, c1 -> c + c1.toChar() }
            }

            fun RandomAccessFile.readLittleEndienShort(): Short {
                return ByteBuffer
                    .wrap(
                        (0..1)
                            .map { this.readByte() }
                            .toByteArray()
                    )
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .short
            }

            fun RandomAccessFile.readLittleEndienInt(): Int {
                return ByteBuffer
                    .wrap(
                        (0..3)
                            .map { this.readByte() }
                            .toByteArray()
                    )
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .int
            }

            RandomAccessFile(filePath, "r")
                .run {
                    val chunkId = this.readLittleEndienInt()
                    val chunkSize = this.readLittleEndienInt()
                    val format = Format.of(this.readString(4))
                    val subChunkId = SubChunkId.of(this.readString(4))
                    val subChunk1Size = this.readLittleEndienInt()
                    val audioFormat = AudioFormat.of(this.readLittleEndienShort())
                    val numChannels: NumChannels = NumChannels.of(this.readLittleEndienShort())
                    val samplingRate: SamplingRate = SamplingRate.of(this.readLittleEndienInt())
                    val byteRate = this.readLittleEndienInt()
                    val blockAlign = this.readLittleEndienShort()
                    val bitPerSample = this.readLittleEndienShort()
                    val subChunk2Id = this.readLittleEndienInt()
                    val subChunk2Size = this.readLittleEndienInt()

                    val fileData =
                        fileByteList
                            .copyOfRange(44, fileByteList.size)
                            .map { it }

                    return WaveModel(
                        data = fileData.map { it.toShort() },
                        format = format,
                        audioFormat = audioFormat,
                        numChannels = numChannels,
                        samplingRate = samplingRate
                    )
                }
        }
    }
}

data class ChunkId(
    val id: ByteArray
) {
    companion object {
        fun default(): ChunkId {
            return ChunkId(
                "RIFF".toByteArray()
            )
        }
    }
}

data class ChunkSize(
    val size: LittleEndianInt
) {
    companion object {
        fun default(): ChunkSize {
            return ChunkSize(
                LittleEndianInt.of(36)
            )
        }
    }
}

data class Format(
    val format: ByteArray
) {
    companion object {
        fun default(): Format {
            return Format(
                "WAVE".toByteArray()
            )
        }

        fun of(
            value: String
        ): Format {
            return Format(
                value.toByteArray()
            )
        }
    }
}

data class SubChunkId(
    val id: ByteArray
) {
    companion object {
        fun default(): SubChunkId {
            return SubChunkId(
                "fmt ".toByteArray()
            )
        }

        fun of(
            value: String
        ): SubChunkId {
            return SubChunkId(
                value.toByteArray()
            )
        }
    }
}

data class FormatSize private constructor(
    val size: LittleEndianInt,
    val rawSize: Int
) {
    companion object {
        fun default(): FormatSize {
            return FormatSize(
                LittleEndianInt.of(16),
                16
            )
        }
    }
}

data class AudioFormat(
    val formatId: ByteArray
) {
    companion object {
        fun default(): AudioFormat {
            return AudioFormat(
                listOf<Byte>(1, 0)
                    .toByteArray()
            )
        }

        fun of(id: Short): AudioFormat {
            val buffer = ByteBuffer
                .allocate(2)
                .putShort(id)

            return AudioFormat(
                buffer.array()
            )
        }
    }
}

sealed class ChannelNumber(
    val channelCount: Short
) {
    object Mono : ChannelNumber(1)
    object Stereo : ChannelNumber(2)
}

data class NumChannels(
    val number: LittleEndianShort,
    val rawNumber: Int
) {
    companion object {
        fun of(channelNumber: ChannelNumber): NumChannels {
            return NumChannels(
                LittleEndianShort.of(channelNumber.channelCount),
                channelNumber.channelCount.toInt()
            )
        }

        fun of(channelCount: Short): NumChannels {
            return NumChannels(
                LittleEndianShort.of(channelCount),
                channelCount.toInt()
            )
        }
    }
}

data class SamplingRate private constructor(
    val rate: LittleEndianInt,
    val rawValue: Int
) {
    companion object {

        const val rate44100: Int = 44100

        fun of(value: Int): SamplingRate {
            return SamplingRate(
                LittleEndianInt.of(
                    value
                ),
                value
            )
        }

        fun default(): SamplingRate {
            return of(rate44100)
        }
    }
}

// SampleRate * NumChannels * BitsPerSample/8
// Data Transfer speed
data class BytePerSecond(
    val value: LittleEndianInt
) {
    companion object {
        fun of(
            samplingRate: SamplingRate,
            bitPerSample: BitPerSample,
            channelCount: NumChannels
        ): BytePerSecond {
            return BytePerSecond(
                LittleEndianInt.of(
                    samplingRate.rawValue * (bitPerSample.rawValue / 8) * channelCount.rawNumber
                )
            )
        }
    }
}

data class BlockSize(
    val size: LittleEndianShort
) {
    companion object {
        // NumChannels * BitsPerSample/8
        fun of(
            numChannels: NumChannels,
            bitPerSample: BitPerSample
        ): BlockSize {
            return BlockSize(
                LittleEndianShort.of(
                    (numChannels.rawNumber * bitPerSample.rawValue / 8).toShort()
                )
            )
        }
    }
}

data class BitPerSample(
    val value: LittleEndianShort,
    val rawValue: Short
) {
    companion object {

        const val eightBit: Short = 8
        const val sixteenBit: Short = 16

        fun of(
            value: Short
        ): BitPerSample {
            return BitPerSample(
                LittleEndianShort.of(value),
                value
            )
        }
    }
}

data class SubChunk2Id(
    val value: ByteArray
) {
    companion object {
        fun default(): SubChunk2Id {
            return SubChunk2Id(
                "data".toByteArray()
            )
        }
    }
}

// NumSamples * NumChannels * BitsPerSample/8
data class SubChunk2Size(
    val size: LittleEndianInt
) {
    companion object {
        fun of(
            numberOfSample: Int,
            numChannels: NumChannels,
            bitPerSample: BitPerSample
        ): SubChunk2Size {
            return SubChunk2Size(
                LittleEndianInt.of(
                    numberOfSample * numChannels.rawNumber * bitPerSample.rawValue / 8
                )
            )
        }
    }
}

