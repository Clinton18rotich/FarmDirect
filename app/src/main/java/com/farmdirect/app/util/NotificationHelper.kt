package com.farmdirect.app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.farmdirect.app.MainActivity
import com.farmdirect.app.R

object NotificationHelper {
    
    private const val CHANNEL_ID = "farmdirect_channel"
    private const val CHANNEL_NAME = "FarmDirect Notifications"
    private const val CHANNEL_DESC = "Order updates, messages, and alerts"
    
    // Notification IDs
    const val ORDER_UPDATE = 1001
    const val NEW_MESSAGE = 1002
    const val DELIVERY_UPDATE = 1003
    const val PAYMENT_CONFIRMATION = 1004
    const val WEATHER_ALERT = 1005
    const val PRICE_ALERT = 1006
    
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableVibration(true)
                setShowBadge(true)
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    
    fun showNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        intent: Intent? = null
    ) {
        val pendingIntent = if (intent != null) {
            PendingIntent.getActivity(
                context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            val defaultIntent = Intent(context, MainActivity::class.java)
            PendingIntent.getActivity(
                context, id, defaultIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(id, notification)
    }
    
    fun showOrderUpdate(context: Context, orderId: String, status: String) {
        showNotification(
            context, ORDER_UPDATE,
            "Order #$orderId",
            "Your order is now $status",
            Intent(context, MainActivity::class.java).apply { putExtra("orderId", orderId) }
        )
    }
    
    fun showNewMessage(context: Context, senderName: String, message: String) {
        showNotification(
            context, NEW_MESSAGE,
            "New message from $senderName",
            message.take(100),
            Intent(context, MainActivity::class.java).apply { putExtra("openChat", true) }
        )
    }
    
    fun showDeliveryUpdate(context: Context, deliveryId: String, status: String) {
        showNotification(
            context, DELIVERY_UPDATE,
            "Delivery Update",
            "Your delivery is $status",
            Intent(context, MainActivity::class.java).apply { putExtra("deliveryId", deliveryId) }
        )
    }
    
    fun showPaymentConfirmation(context: Context, amount: Double) {
        showNotification(
            context, PAYMENT_CONFIRMATION,
            "Payment Confirmed ✅",
            "KES ${"%,.0f".format(amount)} has been processed successfully"
        )
    }
    
    fun showWeatherAlert(context: Context, alert: String) {
        showNotification(
            context, WEATHER_ALERT,
            "🌤️ Weather Alert",
            alert
        )
    }
    
    fun showPriceAlert(context: Context, crop: String, price: Double, trend: String) {
        val emoji = when (trend) { "RISING" -> "📈"; "FALLING" -> "📉"; else -> "📊" }
        showNotification(
            context, PRICE_ALERT,
            "$emoji Price Alert: $crop",
            "Price is now KES ${"%,.0f".format(price)} - $trend"
        )
    }
}
