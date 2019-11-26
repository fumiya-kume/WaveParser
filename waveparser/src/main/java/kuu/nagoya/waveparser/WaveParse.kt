package kuu.nagoya.waveparser

import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

class WaveParse {
    companion object {
        private fun RandomAccessFile.readBytes(count: Int): ByteArray {
            return (1..count)
                .map { this.readByte() }
                .toByteArray()
        }

        private fun RandomAccessFile.toLittleEndienInt(): Int {
            return ByteBuffer.wrap(this.readBytes(4))
                .order(ByteOrder.LITTLE_ENDIAN)
                .int
        }

        private fun RandomAccessFile.toLittleEndienShort(): Short {
            return ByteBuffer.wrap(this.readBytes(2))
                .order(ByteOrder.LITTLE_ENDIAN)
                .short
        }

        private fun Int.toLittleEndianByteList(): ByteArray {
            return ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(this)
                .array()
        }

        private fun Short.toLittleEndianByteList(): ByteArray {
            return ByteBuffer.allocate(2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort(this)
                .array()
        }

        private fun String.toLittleEndianByteList(): ByteArray {
            return this
                .map { it.toByte() }
                .toByteArray()
        }

        private fun RandomAccessFile.readAll(): List<Short> {
            val data = ByteArray((this.length() - this.filePointer).toInt())
            this.read(data)
            return data.map { it.toShort() }
        }


        private fun RandomAccessFile.readString(count: Int): String {
            return (1..count)
                .map { this.readByte().toChar() }
                .fold("") { c, c1 ->
                    c + c1
                }
        }

        fun loadWaveFromFile(file: File): Wave {
            val ras = RandomAccessFile(file, "r")
            return Wave(
                chunkId = ras.readString(4),
                chunkSize = ras.toLittleEndienInt(),
                format = ras.readString(4),
                subChunk1Id = ras.readString(4),
                subChunk1Size = ras.toLittleEndienInt(),
                audioFormat = ras.toLittleEndienShort(),
                numChannels = ras.toLittleEndienShort(),
                sampleRate = ras.toLittleEndienInt(),
                byteRate = ras.toLittleEndienInt(),
                blockAlign = ras.toLittleEndienInt(),
                bitPerSample = ras.toLittleEndienInt(),
                subCHunk2Id = ras.readString(4),
                subChunk2Size = ras.toLittleEndienInt(),
                data = ras.readAll()
            )
        }

        fun storeWaveFile(
            wave: Wave,
            file: File
        ) {

            if (file.exists()) {
                file.delete()
            }

            val ras = FileOutputStream(file)
            ras.write(wave.chunkId.toLittleEndianByteList())
            ras.write(wave.chunkSize.toLittleEndianByteList())
            ras.write(wave.format.toLittleEndianByteList())
            ras.write(wave.subChunk1Id.toLittleEndianByteList())
            ras.write(wave.subChunk1Size.toLittleEndianByteList())
            ras.write(wave.audioFormat.toLittleEndianByteList())
            ras.write(wave.numChannels.toLittleEndianByteList())
            ras.write(wave.sampleRate.toLittleEndianByteList())
            ras.write(wave.byteRate.toLittleEndianByteList())
            ras.write(wave.blockAlign.toLittleEndianByteList())
            ras.write(wave.bitPerSample.toLittleEndianByteList())
            ras.write(wave.subCHunk2Id.toLittleEndianByteList())
            ras.write(wave.subChunk2Size.toLittleEndianByteList())
            ras.write(wave.data.map { it.toByte() }.toByteArray())
            ras.close()

        }

        fun storeWaveFile(
            file: File,
            data: List<Short>,
            samplingRate: Int = 44100,
            bitPerSample: Int = 8,
            numChannels: Short = 1
        ) {
            storeWaveFile(
                Wave(
                    data = data,
                    sampleRate = samplingRate,
                    bitPerSample = bitPerSample,
                    numChannels = numChannels
                ),
                file
            )
        }
    }
}