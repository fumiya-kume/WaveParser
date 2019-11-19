package kuu.nagoya.audiotrack


enum class AudioSource(
    val value: Int
) {
    DEFAULT(0),
    MIC(1),
    VOICE_UPLINK(2),
    VOICE_DOWNLINK(3),
    VOICE_CALL(4),
    CAMCORDER(5),
    VOICE_RECOGNITION(6),
    VOICE_COMMUNICATION(7),
    REMOTE_SUBMIX(8),
    UNPROCESSED(9),
    VOICE_PERFORMANCE(10),
    ECHO_REFERENCE(1997),
    RADIO_TUNER(1998),
    HOTWORD(1999),
}