package com.farmdirect.app.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.farmdirect.app.MainActivity
import com.farmdirect.app.R

object QuickReplyNotification {
    
    private const val KEY_REPLY = "quick_reply_key"
    private const val CHANNEL_MESSAGES = "messages_channel"
    
    fun showMessageNotification(
        context: Context,
        messageId: String,
        senderName: String,
        messageText: String
    ) {
        val replyIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("action", "quick_reply")
            putExtra("messageId", messageId)
        }
        val replyPendingIntent = PendingIntent.getActivity(
            context, messageId.hashCode(), replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val remoteInput = RemoteInput.Builder(KEY_REPLY)
            .setLabel("Reply to $senderName...")
            .build()
        
        val action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()
        
        val notification = NotificationCompat.Builder(context, CHANNEL_MESSAGES)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(senderName)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(action)
            .setAutoCancel(true)
            .build()
        
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        manager.notify(messageId.hashCode(), notification)
    }
    
    fun getReplyText(intent: Intent): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(KEY_REPLY)?.toString()
        }
        return null
    }
}
