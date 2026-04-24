package com.farmdirect.app.data.remote.admin

import android.content.Context
import com.farmdirect.app.util.AuditLogger
import com.farmdirect.app.util.CreatorProtection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatorDashboardAPI(private val context: Context) {
    
    private val _stats = MutableStateFlow(CreatorStats())
    val stats = _stats.asStateFlow()
    
    data class CreatorStats(
        val totalUsers: Int = 0,
        val totalFarmers: Int = 0,
        val totalBuyers: Int = 0,
        val totalRiders: Int = 0,
        val totalListings: Int = 0,
        val totalOrders: Int = 0,
        val totalRevenue: Double = 0.0,
        val platformEarnings: Double = 0.0,
        val activeDisputes: Int = 0,
        val resolvedDisputes: Int = 0,
        val deletionAttempts: Int = 0,
        val securityAlerts: Int = 0,
        val creatorName: String = CreatorProtection.CREATOR_NAME,
        val companyName: String = CreatorProtection.COMPANY_NAME,
        val isCreatorVerified: Boolean = true
    )
    
    fun loadDashboard() {
        val prefs = context.getSharedPreferences("farmdirect_creator", Context.MODE_PRIVATE)
        val deletionAttempts = context.getSharedPreferences("farmdirect_security", Context.MODE_PRIVATE)
            .getInt("creator_deletion_attempts", 0)
        
        _stats.value = CreatorStats(
            totalUsers = prefs.getInt("total_users", 0),
            totalFarmers = prefs.getInt("total_farmers", 0),
            totalBuyers = prefs.getInt("total_buyers", 0),
            totalRiders = prefs.getInt("total_riders", 0),
            deletionAttempts = deletionAttempts,
            securityAlerts = AuditLogger.getSecurityAlerts(context).size,
            isCreatorVerified = CreatorProtection.verifyCreatorIntegrity(context)
        )
    }
    
    fun getCreatorInfo(): String {
        return """
            👑 CREATOR DASHBOARD
            
            Name: ${CreatorProtection.CREATOR_NAME}
            Company: ${CreatorProtection.COMPANY_NAME}
            Founded: ${CreatorProtection.FOUNDING_DATE}
            Creator ID: ${CreatorProtection.CREATOR_ID}
            
            Account Status: 🔒 PROTECTED (Cannot be deleted)
            Integrity Check: ✅ VERIFIED
            
            Your account is permanently protected as the FarmDirect creator.
        """.trimIndent()
    }
    
    fun logCreatorAction(action: String) {
        AuditLogger.log(context, AuditLogger.AuditEntry(
            action = AuditLogger.AuditAction.ADMIN_ACTION,
            userId = CreatorProtection.CREATOR_ID,
            userName = CreatorProtection.CREATOR_NAME,
            details = action
        ))
    }
}
