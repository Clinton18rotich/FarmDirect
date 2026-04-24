package com.farmdirect.app.domain.model

data class BlockchainRecord(
    val id: String = "", val recordType: String = "SALE", val userId: String = "",
    val description: String = "", val amount: Double = 0.0, val transactionHash: String = "",
    val verified: Boolean = false, val timestamp: Long = System.currentTimeMillis()
)

data class BlockchainVerification(val recordId: String = "", val isAuthentic: Boolean = false, val storedHash: String = "", val recalculatedHash: String = "")

data class AuditReport(val userId: String = "", val totalTransactions: Int = 0, val totalRevenue: Double = 0.0, val totalExpenses: Double = 0.0, val netProfit: Double = 0.0, val blockchainVerified: Boolean = false)

data class ProfitLossStatement(val userId: String = "", val totalRevenue: Double = 0.0, val totalExpenses: Double = 0.0, val netProfit: Double = 0.0, val blockchainHash: String = "")
