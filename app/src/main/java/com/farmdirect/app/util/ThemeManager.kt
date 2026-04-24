package com.farmdirect.app.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ThemeManager {
    
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()
    
    private const val PREFS_NAME = "farmdirect_prefs"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_FOLLOW_SYSTEM = "follow_system"
    
    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val followSystem = prefs.getBoolean(KEY_FOLLOW_SYSTEM, true)
        val darkMode = prefs.getBoolean(KEY_DARK_MODE, false)
        
        if (followSystem) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            AppCompatDelegate.setDefaultNightMode(
                if (darkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        _isDarkMode.value = darkMode
    }
    
    fun setDarkMode(context: Context, enabled: Boolean) {
        _isDarkMode.value = enabled
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .putBoolean(KEY_FOLLOW_SYSTEM, false)
            .apply()
    }
    
    fun setFollowSystem(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_FOLLOW_SYSTEM, true)
            .apply()
    }
    
    fun toggleDarkMode(context: Context) {
        setDarkMode(context, !_isDarkMode.value)
    }
    
    fun isCurrentlyDarkMode(): Boolean = _isDarkMode.value
}
