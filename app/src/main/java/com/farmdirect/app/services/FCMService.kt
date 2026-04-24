package com.farmdirect.app.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.farmdirect.app.MainActivity
import com.farmdirect.app.R
import com.farmdirect.app.util.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to your server
        saveTokenToServer(token)
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        when (message.data["type"]) {
            "order_update" -> handleOrderUpdate(message)
            "new_message" -> handleNewMessage(message)
            "price_alert" -> handlePriceAlert(message)
            "weather_alert" -> handleWeatherAlert(message)
            "delivery_update" -> handleDeliveryUpdate(message)
            "referral_completed" -> handleReferralCompleted(message)
            "promotion" -> handlePromotion(message)
            else -> handleGeneralNotification(message)
        }
    }
    
    private fun handleOrderUpdate(message: RemoteMessage) {
        val orderId = message.data["orderId"] ?: return
        val status = message.data["status"] ?: return
        NotificationHelper.showOrderUpdate(this, orderId, status)
    }
    
    private fun handleNewMessage(message: RemoteMessage) {
        val senderName = message.data["senderName"] ?: "Someone"
        val msg = message.data["message"] ?: "Sent you a message"
        NotificationHelper.showNewMessage(this, senderName, msg)
    }
    
    private fun handlePriceAlert(message: RemoteMessage) {
        val crop = message.data["crop"] ?: return
        val price = message.data["price"]?.toDoubleOrNull() ?: return
        val trend = message.data["trend"] ?: "STABLE"
        NotificationHelper.showPriceAlert(this, crop, price, trend)
    }
    
    private fun handleWeatherAlert(message: RemoteMessage) {
        val alert = message.data["alert"] ?: "Weather alert for your area"
        NotificationHelper.showWeatherAlert(this, alert)
    }
    
    private fun handleDeliveryUpdate(message: RemoteMessage) {
        val deliveryId = message.data["deliveryId"] ?: return
        val status = message.data["status"] ?: return
        NotificationHelper.showDeliveryUpdate(this, deliveryId, status)
    }
    
    private fun handleReferralCompleted(message: RemoteMessage) {
        val name = message.data["referredName"] ?: "Someone"
        showNotification(
            title = "🎉 Referral Bonus!",
            message = "$name completed their first sale. You earned KES 100!"
        )
    }
    
    private fun handlePromotion(message: RemoteMessage) {
        val title = message.data["title"] ?: "Special Offer"
        val body = message.data["body"] ?: "Check out our latest deals"
        showNotification(title = title, message = body)
    }
    
    private fun handleGeneralNotification(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: "FarmDirect"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        showNotification(title = title, message = body)
    }
    
    private fun showNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, "farmdirect_channel")
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
    
    private fun saveTokenToServer(token: String) {
        // Send to your backend API
        // RetrofitClient.apiService.updateFCMToken(token)
    }
}
