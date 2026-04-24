package com.farmdirect.app.util

import android.content.Context
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

object AccessibilityManager {
    
    fun init(context: Context) {
        // Ensure all accessibility services are configured
    }
    
    fun getContentDescription(screen: String, element: String): String {
        return when (screen) {
            "home" -> when (element) {
                "search" -> "Search for crops, livestock, and products"
                "categories" -> "Browse by category"
                "crop_card" -> "View crop details"
                else -> element
            }
            "profile" -> when (element) {
                "avatar" -> "Your profile picture"
                "edit" -> "Edit your profile"
                "logout" -> "Sign out of your account"
                else -> element
            }
            else -> element
        }
    }
    
    fun getAnnouncement(message: String): String {
        return "FarmDirect announcement: $message"
    }
}

fun Modifier.accessibilityLabel(label: String): Modifier {
    return this.semantics { contentDescription = label }
}
