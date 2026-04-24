package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.AuditReport
import com.farmdirect.app.domain.model.BlockchainRecord
import com.farmdirect.app.domain.model.BlockchainVerification
import com.farmdirect.app.domain.model.ProfitLossStatement
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlockchainRepository @Inject constructor() {
    suspend fun getRecords(token: String, userId: String): List<BlockchainRecord> {
        val response = RetrofitClient.apiService.getBlockchainRecords("Bearer $token", userId)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun verifyRecord(token: String, recordId: String): BlockchainVerification? {
        val response = RetrofitClient.apiService.verifyBlockchainRecord("Bearer $token", recordId)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun getAuditReport(token: String, userId: String, year: Int): AuditReport? {
        val response = RetrofitClient.apiService.getAuditReport("Bearer $token", userId, year)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun getProfitLoss(token: String, userId: String, period: String): ProfitLossStatement? {
        val response = RetrofitClient.apiService.getProfitLoss("Bearer $token", userId, period)
        return if (response.isSuccessful) response.body() else null
    }
}
