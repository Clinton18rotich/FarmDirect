package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.io.File
import java.util.UUID

class ReferralManager(private val context: Context) {
    
    private val _referralStats = MutableStateFlow(ReferralStats())
    val referralStats = _referralStats.asStateFlow()
    
    private val dataFile: File
        get() = File(context.filesDir, "referrals.json")
    
    data class ReferralStats(
        val referralCode: String = "",
        val totalReferrals: Int = 0,
        val successfulReferrals: Int = 0,
        val pendingReferrals: Int = 0,
        val totalEarned: Double = 0.0,
        val referralHistory: List<ReferralRecord> = emptyList(),
        val rank: String = "🌱 Seedling"
    )
    
    data class ReferralRecord(
        val id: String = UUID.randomUUID().toString(),
        val referredPhone: String = "",
        val referredName: String = "",
        val status: ReferralStatus = ReferralStatus.PENDING,
        val reward: Double = 100.0,  // KES 100 per successful referral
        val dateReferred: Long = System.currentTimeMillis(),
        val dateCompleted: Long? = null
    )
    
    enum class ReferralStatus {
        PENDING,    // Invited but not joined
        JOINED,     // Created account
        ACTIVE,     // Made first sale/purchase
        PAID        // Reward paid
    }
    
    // Referral rewards by milestone
    val rewards = mapOf(
        "signup" to 50.0,     // KES 50 when friend signs up
        "first_sale" to 100.0, // KES 100 when friend completes first sale
        "first_buy" to 100.0,  // KES 100 when friend makes first purchase
        "premium" to 200.0     // KES 200 when friend subscribes to premium
    )
    
    // Rank system
    val ranks = mapOf(
        0 to "🌱 Seedling",
        5 to "🌿 Grower",
        10 to "🌾 Harvester",
        25 to "🚜 Farmer",
        50 to "🏆 Agri-Preneur",
        100 to "👑 Farm King",
        500 to "🌟 FarmDirect Legend"
    )
    
    init {
        loadData()
        if (_referralStats.value.referralCode.isEmpty()) {
            generateReferralCode()
        }
    }
    
    fun generateReferralCode(): String {
        val code = "FD${(1000..9999).random()}"
        _referralStats.value = _referralStats.value.copy(referralCode = code)
        saveData()
        return code
    }
    
    fun getReferralLink(): String {
        val code = _referralStats.value.referralCode
        return "https://farmdirect.africa/ref/$code"
    }
    
    fun getReferralMessage(): String {
        val code = _referralStats.value.referralCode
        return """
            🌾 Join me on FarmDirect!
            
            Sell directly to buyers, get fair prices, and grow your farm business.
            
            Use my referral code: $code
            Or click: https://farmdirect.africa/ref/$code
            
            We both earn KES 100 when you complete your first sale! 🎉
        """.trimIndent()
    }
    
    fun addReferral(phone: String, name: String = "") {
        val record = ReferralRecord(referredPhone = phone, referredName = name)
        val history = _referralStats.value.referralHistory + record
        _referralStats.value = _referralStats.value.copy(
            totalReferrals = history.size,
            pendingReferrals = history.count { it.status == ReferralStatus.PENDING },
            referralHistory = history
        )
        updateRank()
        saveData()
    }
    
    fun updateReferralStatus(referralId: String, status: ReferralStatus) {
        val history = _referralStats.value.referralHistory.map { record ->
            if (record.id == referralId) {
                record.copy(
                    status = status,
                    dateCompleted = if (status == ReferralStatus.PAID) System.currentTimeMillis() else record.dateCompleted
                )
            } else record
        }
        
        val totalEarned = history.sumOf {
            when (it.status) {
                ReferralStatus.PAID -> it.reward
                ReferralStatus.ACTIVE -> it.reward
                else -> 0.0
            }
        }
        
        _referralStats.value = _referralStats.value.copy(
            referralHistory = history,
            successfulReferrals = history.count { it.status == ReferralStatus.PAID || it.status == ReferralStatus.ACTIVE },
            pendingReferrals = history.count { it.status == ReferralStatus.PENDING },
            totalEarned = totalEarned
        )
        updateRank()
        saveData()
    }
    
    fun shareReferral(context: Context) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "🌾 Join FarmDirect - Africa's Agricultural Super App")
            putExtra(Intent.EXTRA_TEXT, getReferralMessage())
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }
    
    private fun updateRank() {
        val successful = _referralStats.value.successfulReferrals
        val rank = ranks.entries
            .sortedByDescending { it.key }
            .firstOrNull { successful >= it.key }
            ?.value ?: "🌱 Seedling"
        _referralStats.value = _referralStats.value.copy(rank = rank)
    }
    
    private fun loadData() {
        try {
            if (dataFile.exists()) {
                val json = JSONObject(dataFile.readText())
                _referralStats.value = ReferralStats(
                    referralCode = json.optString("referralCode", ""),
                    totalReferrals = json.optInt("totalReferrals", 0),
                    successfulReferrals = json.optInt("successfulReferrals", 0),
                    pendingReferrals = json.optInt("pendingReferrals", 0),
                    totalEarned = json.optDouble("totalEarned", 0.0)
                )
            }
        } catch (e: Exception) { }
    }
    
    private fun saveData() {
        try {
            val json = JSONObject().apply {
                put("referralCode", _referralStats.value.referralCode)
                put("totalReferrals", _referralStats.value.totalReferrals)
                put("successfulReferrals", _referralStats.value.successfulReferrals)
                put("pendingReferrals", _referralStats.value.pendingReferrals)
                put("totalEarned", _referralStats.value.totalEarned)
            }
            dataFile.writeText(json.toString())
        } catch (e: Exception) { }
    }
}
