package com.farmdirect.app.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import com.farmdirect.app.domain.model.Crop
import com.google.gson.Gson

class SMSBridge : BroadcastReceiver() {
    
    private val gson = Gson()
    
    companion object {
        const val SMS_COMMAND_PREFIX = "FD"
        
        // SMS Commands
        const val CMD_HELP = "HELP"
        const val CMD_PRICE = "PRICE"
        const val CMD_BUY = "BUY"
        const val CMD_SELL = "SELL"
        const val CMD_WEATHER = "WEATHER"
        const val CMD_ORDERS = "ORDERS"
        const val CMD_LOCATION = "LOC"
        
        fun sendSMS(phoneNumber: String, message: String) {
            val smsManager = SmsManager.getDefault()
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
        }
        
        fun formatPriceResponse(prices: Map<String, Double>): String {
            return buildString {
                appendLine("📊 FarmDirect Prices:")
                appendLine("━━━━━━━━━━━━━━━━━")
                prices.forEach { (crop, price) ->
                    appendLine("$crop: KES ${"%,.0f".format(price)}")
                }
                appendLine("━━━━━━━━━━━━━━━━━")
                appendLine("Reply FD BUY [crop] for listings")
                appendLine("Reply FD SELL [qty] [crop] [price] to post")
            }
        }
        
        fun formatCropListings(crops: List<Crop>, cropType: String): String {
            if (crops.isEmpty()) return "No $cropType listings found nearby."
            return buildString {
                appendLine("🌽 $cropType Listings:")
                crops.take(5).forEach { crop ->
                    appendLine("• ${crop.farmerName}: ${crop.quantity}${crop.unit} @ KES ${"%,.0f".format(crop.pricePerUnit)}")
                    appendLine("  📍 ${crop.farmerLocation.take(20)}")
                }
                if (crops.size > 5) appendLine("...and ${crops.size - 5} more on the app")
                appendLine("📱 Download FarmDirect for full access")
            }
        }
        
        fun formatWeatherResponse(weather: String): String {
            return buildString {
                appendLine("🌤️ FarmDirect Weather:")
                appendLine(weather)
                appendLine("📱 More details on FarmDirect app")
            }
        }
        
        fun getHelpMessage(): String {
            return """
                🌾 FarmDirect SMS Commands:
                
                FD PRICE [crop] - Check prices
                FD BUY [crop] - Find sellers
                FD SELL [qty] [crop] [price] - Post listing
                FD WEATHER [location] - Get forecast
                FD ORDERS - Check your orders
                FD HELP - Show this help
                
                Example: FD PRICE MAIZE
                Example: FD SELL 500 MAIZE 3500
                
                📱 Download the app for full features!
            """.trimIndent()
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (message in messages) {
            val sender = message.originatingAddress ?: continue
            val body = message.messageBody ?: continue
            
            if (!body.startsWith("$SMS_COMMAND_PREFIX ")) continue
            
            val parts = body.removePrefix("$SMS_COMMAND_PREFIX ").trim().split(" ")
            val command = parts.getOrNull(0)?.uppercase() ?: continue
            
            when (command) {
                CMD_HELP -> sendSMS(sender, getHelpMessage())
                CMD_PRICE -> {
                    val crop = parts.getOrNull(1) ?: "MAIZE"
                    val prices = mapOf("MAIZE" to 3500.0, "BEANS" to 8000.0, "POTATOES" to 2500.0)
                    val filtered = if (crop == "ALL") prices else prices.filter { it.key.equals(crop, true) }
                    sendSMS(sender, formatPriceResponse(filtered))
                }
                CMD_BUY -> {
                    val crop = parts.getOrNull(1) ?: "MAIZE"
                    sendSMS(sender, "🔍 Searching for $crop near you...\n📱 Open FarmDirect app for full listings")
                }
                CMD_WEATHER -> {
                    val location = parts.drop(1).joinToString(" ").ifEmpty { "Kitale" }
                    sendSMS(sender, "🌤️ Weather for $location:\n☀️ 24°C, Sunny\n💧 Humidity: 65%\n🌬️ Wind: 12 km/h")
                }
                CMD_ORDERS -> {
                    sendSMS(sender, "📋 Your recent orders:\n📱 Open FarmDirect app for full details")
                }
                CMD_SELL -> {
                    val qty = parts.getOrNull(1) ?: ""
                    val crop = parts.getOrNull(2) ?: ""
                    val price = parts.getOrNull(3) ?: ""
                    if (qty.isNotEmpty() && crop.isNotEmpty() && price.isNotEmpty()) {
                        sendSMS(sender, "✅ Listing posted!\n$qty $crop @ KES $price\n📱 View on FarmDirect app")
                    } else {
                        sendSMS(sender, "❌ Format: FD SELL [quantity] [crop] [price]\nExample: FD SELL 500 MAIZE 3500")
                    }
                }
                else -> sendSMS(sender, "❓ Unknown command. Reply FD HELP for options.")
            }
        }
    }
}
