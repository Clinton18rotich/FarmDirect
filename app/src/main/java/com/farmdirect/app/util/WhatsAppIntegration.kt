package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import android.net.Uri

class WhatsAppIntegration(private val context: Context) {
    
    fun shareListing(
        cropName: String,
        quantity: String,
        price: String,
        location: String,
        listingUrl: String
    ) {
        val message = """
            🌾 *Fresh Produce Available!*
            
            *$cropName*
            📦 Quantity: $quantity
            💰 Price: $price
            📍 Location: $location
            
            📱 View on FarmDirect: $listingUrl
            
            _Download FarmDirect to see more listings!_
        """.trimIndent()
        
        openWhatsApp(message)
    }
    
    fun shareReferral(referralCode: String, referralLink: String) {
        val message = """
            🌾 *Join me on FarmDirect!*
            
            Sell directly to buyers, get fair prices, and grow your farm business.
            
            Use my referral code: *$referralCode*
            Or click: $referralLink
            
            We both earn KES 100! 🎉
        """.trimIndent()
        
        openWhatsApp(message)
    }
    
    fun shareDeliveryUpdate(
        orderId: String,
        status: String,
        trackingUrl: String
    ) {
        val message = """
            📦 *Delivery Update*
            
            Order: #$orderId
            Status: $status
            
            Track live: $trackingUrl
        """.trimIndent()
        
        openWhatsApp(message)
    }
    
    fun contactSupport(issue: String) {
        val message = "Hello FarmDirect Support,\n\n$issue\n\nPlease help. Thank you."
        openWhatsApp(message, "+254800123456")
    }
    
    fun openWhatsApp(message: String, phoneNumber: String? = null) {
        try {
            val url = if (phoneNumber != null) {
                "https://api.whatsapp.com/send?phone=$phoneNumber&text=${Uri.encode(message)}"
            } else {
                "https://api.whatsapp.com/send?text=${Uri.encode(message)}"
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            // WhatsApp not installed - share via regular share intent
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
                `package` = "com.whatsapp"
            }
            try {
                context.startActivity(intent)
            } catch (e2: Exception) {
                // Fallback to any sharing app
                val fallbackIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                }
                context.startActivity(Intent.createChooser(fallbackIntent, "Share via"))
            }
        }
    }
    
    fun isWhatsAppInstalled(): Boolean {
        return try {
            context.packageManager.getPackageInfo("com.whatsapp", 0)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun shareToWhatsAppGroup(groupName: String, message: String) {
        openWhatsApp(message)
    }
}
