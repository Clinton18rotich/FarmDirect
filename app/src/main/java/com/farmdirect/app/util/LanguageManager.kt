package com.farmdirect.app.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    
    val supportedLanguages = mapOf(
        "en" to "English" to "🇬🇧",
        "sw" to "Kiswahili" to "🇰🇪",
        "ki" to "Gĩkũyũ" to "🇰🇪",
        "luo" to "Dholuo" to "🇰🇪",
        "kln" to "Kalenjin" to "🇰🇪",
        "luy" to "Luhya" to "🇰🇪",
        "kam" to "Kamba" to "🇰🇪",
        "fr" to "Français" to "🇫🇷",
        "pt" to "Português" to "🇲🇿",
        "ar" to "العربية" to "🇸🇩",
        "ha" to "Hausa" to "🇳🇬",
        "yo" to "Yorùbá" to "🇳🇬",
        "zu" to "isiZulu" to "🇿🇦",
        "am" to "አማርኛ" to "🇪🇹",
        "so" to "Soomaali" to "🇸🇴"
    )
    
    fun setLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        
        // Save preference
        context.getSharedPreferences("farmdirect_prefs", Context.MODE_PRIVATE)
            .edit().putString("language", languageCode).apply()
    }
    
    fun getCurrentLanguage(context: Context): String {
        return context.getSharedPreferences("farmdirect_prefs", Context.MODE_PRIVATE)
            .getString("language", "en") ?: "en"
    }
    
    fun getLanguageName(code: String): String {
        return supportedLanguages.entries.find { it.key.first == code }?.key?.second ?: "English"
    }
    
    fun getLanguageFlag(code: String): String {
        return supportedLanguages.entries.find { it.key.first == code }?.value ?: "🇬🇧"
    }
}
