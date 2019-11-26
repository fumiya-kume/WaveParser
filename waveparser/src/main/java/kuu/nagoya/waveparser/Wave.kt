package kuu.nagoya.waveparser


data class Wave(
    val data: List<Short>,
    val chunkId: String = "RIFF",
    val chunkSize: Int = data.size - 8,
    val format: String = "WAVE",
    val subChunk1Id: String = "fmt ",
    val subChunk1Size: Int = 16,
    val audioFormat: Short = 1,
    val numChannels: Short = 1,
    val sampleRate: Int = 44100,
    val bitPerSample: Int = 8,
    val byteRate: Int = sampleRate * numChannels * bitPerSample / 8,
    val blockAlign: Int = numChannels * bitPerSample / 8,
    val subCHunk2Id: String = "data",
    val subChunk2Size: Int = data.size - 44
)

