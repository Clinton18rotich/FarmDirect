package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AlertDialog
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppRatingManager(private val context: Context) {
    
    private val prefs = context.getSharedPreferences("farmdirect_prefs", Context.MODE_PRIVATE)
    private val reviewManager = ReviewManagerFactory.create(context)
    
    private val _shouldShowRating = MutableStateFlow(false)
    val shouldShowRating = _shouldShowRating.asStateFlow()
    
    companion object {
        private const val KEY_LAUNCH_COUNT = "launch_count"
        private const val KEY_LAST_RATING_PROMPT = "last_rating_prompt"
        private const val KEY_HAS_RATED = "has_rated"
        private const val MIN_LAUNCHES = 5
        private const val MIN_DAYS_BETWEEN_PROMPTS = 30
    }
    
    fun onAppLaunched() {
        val launchCount = prefs.getInt(KEY_LAUNCH_COUNT, 0) + 1
        prefs.edit().putInt(KEY_LAUNCH_COUNT, launchCount).apply()
        
        if (shouldPromptForRating()) {
            _shouldShowRating.value = true
        }
    }
    
    private fun shouldPromptForRating(): Boolean {
        val hasRated = prefs.getBoolean(KEY_HAS_RATED, false)
        if (hasRated) return false
        
        val launchCount = prefs.getInt(KEY_LAUNCH_COUNT, 0)
        if (launchCount < MIN_LAUNCHES) return false
        
        val lastPrompt = prefs.getLong(KEY_LAST_RATING_PROMPT, 0)
        val daysSinceLastPrompt = (System.currentTimeMillis() - lastPrompt) / (24 * 60 * 60 * 1000)
        if (daysSinceLastPrompt < MIN_DAYS_BETWEEN_PROMPTS) return false
        
        return true
    }
    
    fun showRatingDialog(onRate: () -> Unit, onLater: () -> Unit, onNever: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("🌟 Enjoying FarmDirect?")
            .setMessage("Your feedback helps us improve and support more farmers across Africa!")
            .setPositiveButton("Rate Now ⭐") { _, _ ->
                prefs.edit().putBoolean(KEY_HAS_RATED, true).apply()
                onRate()
                openPlayStore()
            }
            .setNeutralButton("Later") { _, _ ->
                prefs.edit().putLong(KEY_LAST_RATING_PROMPT, System.currentTimeMillis()).apply()
                onLater()
            }
            .setNegativeButton("Never") { _, _ ->
                prefs.edit().putBoolean(KEY_HAS_RATED, true).apply()
                onNever()
            }
            .show()
    }
    
    private fun openPlayStore() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.farmdirect.app"))
            context.startActivity(intent)
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.farmdirect.app"))
            context.startActivity(intent)
        }
    }
    
    fun openFeedbackForm() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://farmdirect.africa/feedback"))
        context.startActivity(intent)
    }
}
