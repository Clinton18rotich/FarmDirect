package com.farmdirect.app.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*

object CreatorProtection {
    
    // ═══════════════════════════════════════
    // CREATOR IDENTITY (IMMUTABLE)
    // ═══════════════════════════════════════
    const val CREATOR_NAME = "Clinton Rotich"
    const val CREATOR_PHONE = "+254704519744"  // Replace with actual
    const val CREATOR_EMAIL = "creator@farmdirect.africa"
    const val CREATOR_ID = "FARM_DIRECT_CREATOR_001"
    const val COMPANY_NAME = "FarmDirect Technologies Ltd"
    const val FOUNDING_DATE = "January 2026"
    
    private const val PREFS_NAME = "farmdirect_creator"
    private const val KEY_CREATOR_VERIFIED = "creator_verified"
    private const val KEY_CREATOR_SIGNATURE = "creator_signature"
    
    fun initialize(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Set immutable creator data
        prefs.edit()
            .putString("creator_name", CREATOR_NAME)
            .putString("creator_phone", CREATOR_PHONE)
            .putString("creator_email", CREATOR_EMAIL)
            .putString("creator_id", CREATOR_ID)
            .putString("company_name", COMPANY_NAME)
            .putString("founding_date", FOUNDING_DATE)
            .putBoolean(KEY_CREATOR_VERIFIED, true)
            .putString(KEY_CREATOR_SIGNATURE, generateCreatorSignature())
            .apply()
    }
    
    fun isCreator(phoneNumber: String): Boolean {
        return phoneNumber == CREATOR_PHONE
    }
    
    fun isCreatorById(userId: String): Boolean {
        return userId == CREATOR_ID
    }
    
    fun getCreatorInfo(): Map<String, String> {
        return mapOf(
            "name" to CREATOR_NAME,
            "phone" to CREATOR_PHONE,
            "email" to CREATOR_EMAIL,
            "company" to COMPANY_NAME,
            "founded" to FOUNDING_DATE
        )
    }
    
    fun preventAccountDeletion(context: Context, phoneNumber: String): Boolean {
        if (isCreator(phoneNumber)) {
            // Log attempt
            val prefs = context.getSharedPreferences("farmdirect_security", Context.MODE_PRIVATE)
            val attempts = prefs.getInt("creator_deletion_attempts", 0) + 1
            prefs.edit().putInt("creator_deletion_attempts", attempts).apply()
            return false // Cannot delete
        }
        return true // Can delete (regular user)
    }
    
    private fun generateCreatorSignature(): String {
        val data = "$CREATOR_NAME|$CREATOR_ID|$FOUNDING_DATE|$COMPANY_NAME"
        return data.hashCode().toString()
    }
    
    fun verifyCreatorIntegrity(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val storedSignature = prefs.getString(KEY_CREATOR_SIGNATURE, "")
        return storedSignature == generateCreatorSignature()
    }
}

@Composable
fun CreatorBadge() {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = AccentGold.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("👑", fontSize = 16.sp)
            Spacer(Modifier.width(6.dp))
            Text(
                "Created by ${CreatorProtection.CREATOR_NAME}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                color = AccentGold
            )
        }
    }
}

@Composable
fun CreatorAttribution() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "🌾 FarmDirect",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            )
            Text(
                "Created by ${CreatorProtection.CREATOR_NAME}",
                fontSize = 13.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Text(
                "${CreatorProtection.COMPANY_NAME} • ${CreatorProtection.FOUNDING_DATE}",
                fontSize = 11.sp,
                color = TextSecondary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
