package com.farmdirect.app.util

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormatimport java.util.*

object AuditLogger {
    
    private const val LOG_FILE = "audit_log.json"
    private const val MAX_LOGS = 1000
    
    enum class AuditAction {
        USER_REGISTERED,
        USER_DELETED,
        CREATOR_DELETION_ATTEMPTED,
        CREATOR_DELETION_BLOCKED,
        LISTING_CREATED,
        LISTING_DELETED,
        ORDER_PLACED,
        ORDER_COMPLETED,
        PAYMENT_MADE,
        PAYMENT_RECEIVED,
        DISPUTE_OPENED,
        DISPUTE_RESOLVED,
        ADMIN_ACTION,
        SECURITY_ALERT
    }
    
    data class AuditEntry(
        val action: AuditAction,
        val userId: String = "system",
        val userName: String = "System",
        val details: String = "",
        val ipAddress: String = "",
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun log(context: Context, entry: AuditEntry) {
        try {
            val file = File(context.filesDir, LOG_FILE)
            val existing = if (file.exists()) {
                JSONArray(file.readText())
            } else {
                JSONArray()
            }
            
            val jsonEntry = JSONObject().apply {
                put("action", entry.action.name)
                put("userId", entry.userId)
                put("userName", entry.userName)
                put("details", entry.details)
                put("ipAddress", entry.ipAddress)
                put("timestamp", entry.timestamp)
                put("formattedDate", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(entry.timestamp)))
            }
            
            existing.put(jsonEntry)
            
            // Keep only last MAX_LOGS entries
            while (existing.length() > MAX_LOGS) {
                existing.remove(0)
            }
            
            file.writeText(existing.toString())
            
            // If creator deletion attempted, send alert
            if (entry.action == AuditAction.CREATOR_DELETION_ATTEMPTED) {
                handleSecurityAlert(context, entry)
            }
        } catch (e: Exception) {
            // Audit logging should never crash the app
        }
    }
    
    private fun handleSecurityAlert(context: Context, entry: AuditEntry) {
        // Notify creator via all channels
        val message = """
            🚨 SECURITY ALERT
            
            Someone attempted to delete the FarmDirect creator account!
            
            Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date(entry.timestamp))}
            User: ${entry.userName}
            Details: ${entry.details}
            
            Action: Account deletion BLOCKED by Creator Protection System.
        """.trimIndent()
        
        // Log to security file
        val securityFile = File(context.filesDir, "security_alerts.json")
        val alerts = if (securityFile.exists()) {
            JSONArray(securityFile.readText())
        } else {
            JSONArray()
        }
        alerts.put(JSONObject().apply {
            put("type", "CREATOR_DELETION_ATTEMPTED")
            put("message", message)
            put("timestamp", System.currentTimeMillis())
        })
        securityFile.writeText(alerts.toString())
    }
    
    fun getAuditLogs(context: Context): List<AuditEntry> {
        try {
            val file = File(context.filesDir, LOG_FILE)
            if (!file.exists()) return emptyList()
            
            val entries = JSONArray(file.readText())
            val result = mutableListOf<AuditEntry>()
            
            for (i in 0 until entries.length()) {
                val entry = entries.getJSONObject(i)
                result.add(AuditEntry(
                    action = AuditAction.valueOf(entry.getString("action")),
                    userId = entry.optString("userId", "system"),
                    userName = entry.optString("userName", "System"),
                    details = entry.optString("details", ""),
                    timestamp = entry.optLong("timestamp", 0)
                ))
            }
            
            return result.sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            return emptyList()
        }
    }
    
    fun getSecurityAlerts(context: Context): List<String> {
        try {
            val file = File(context.filesDir, "security_alerts.json")
            if (!file.exists()) return emptyList()
            
            val alerts = JSONArray(file.readText())
            val result = mutableListOf<String>()
            
            for (i in 0 until alerts.length()) {
                result.add(alerts.getJSONObject(i).getString("message"))
            }
            
            return result
        } catch (e: Exception) {
            return emptyList()
        }
    }
}
