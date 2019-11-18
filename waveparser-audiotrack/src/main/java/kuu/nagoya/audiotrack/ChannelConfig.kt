package kuu.nagoya.audiotrack

import android.media.AudioFormat

enum class ChannelConfig(val value: Int) {
    CHANNEL_IN_MONO(AudioFormat.CHANNEL_IN_FRONT),
    CHANNEL_IN_STEREO(AudioFormat.CHANNEL_IN_LEFT or AudioFormat.CHANNEL_IN_RIGHT)
}