package com.farmdirect.app.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.farmdirect.app.R

class SoundEffectsManager(private val context: Context) {
    
    private val soundPool: SoundPool
    private val sounds = mutableMapOf<String, Int>()
    private var enabled = true
    
    init {
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .setAudioAttributes(attributes)
            .build()
    }
    
    fun loadSounds() {
        try {
            sounds["click"] = soundPool.load(context, R.raw.click_sound, 1)
            sounds["success"] = soundPool.load(context, R.raw.success_sound, 1)
            sounds["error"] = soundPool.load(context, R.raw.error_sound, 1)
            sounds["notification"] = soundPool.load(context, R.raw.notification_sound, 1)
            sounds["achievement"] = soundPool.load(context, R.raw.achievement_sound, 1)
        } catch (e: Exception) {
            // Sound files not found - silent mode
        }
    }
    
    fun playClick() { play("click") }
    fun playSuccess() { play("success") }
    fun playError() { play("error") }
    fun playNotification() { play("notification") }
    fun playAchievement() { play("achievement") }
    
    private fun play(soundName: String) {
        if (!enabled) return
        sounds[soundName]?.let { soundId ->
            soundPool.play(soundId, 0.5f, 0.5f, 1, 0, 1.0f)
        }
    }
    
    fun setEnabled(enabled: Boolean) { this.enabled = enabled }
    fun isEnabled(): Boolean = enabled
    
    fun release() {
        soundPool.release()
    }
}
