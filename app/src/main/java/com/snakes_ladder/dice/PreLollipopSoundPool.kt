package com.snakes_ladder.dice

import android.media.AudioManager
import android.media.SoundPool

/**
 * Created a pre Lollipop SoundPool
 */
internal object PreLollipopSoundPool {
    fun NewSoundPool(): SoundPool {
        return SoundPool(1, AudioManager.STREAM_MUSIC, 0)
    }
}