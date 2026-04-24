package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import com.farmdirect.app.MainActivity

class AppShortcutsManager(private val context: Context) {
    
    fun createShortcuts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)
        
        val shortcuts = listOf(
            ShortcutInfo.Builder(context, "post_crop")
                .setShortLabel("Post Crop")
                .setLongLabel("Post Your Harvest")
                .setIcon(Icon.createWithResource(context, android.R.drawable.ic_input_add))
                .setIntent(Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("shortcut", "post_crop")
                })
                .build(),
            
            ShortcutInfo.Builder(context, "check_prices")
                .setShortLabel("Check Prices")
                .setLongLabel("Check Market Prices")
                .setIcon(Icon.createWithResource(context, android.R.drawable.ic_menu_search))
                .setIntent(Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("shortcut", "check_prices")
                })
                .build(),
            
            ShortcutInfo.Builder(context, "weather")
                .setShortLabel("Weather")
                .setLongLabel("Check Weather")
                .setIcon(Icon.createWithResource(context, android.R.drawable.ic_menu_compass))
                .setIntent(Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("shortcut", "weather")
                })
                .build(),
            
            ShortcutInfo.Builder(context, "my_orders")
                .setShortLabel("My Orders")
                .setLongLabel("View My Orders")
                .setIcon(Icon.createWithResource(context, android.R.drawable.ic_menu_recent_history))
                .setIntent(Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("shortcut", "my_orders")
                })
                .build()
        )
        
        shortcutManager.dynamicShortcuts = shortcuts
    }
    
    fun handleShortcut(shortcutId: String, onAction: (String) -> Unit) {
        when (shortcutId) {
            "post_crop", "check_prices", "weather", "my_orders" -> onAction(shortcutId)
        }
    }
}
