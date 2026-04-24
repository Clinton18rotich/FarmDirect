package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Dispute
import com.farmdirect.app.domain.model.OpenDisputeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DisputeRepository @Inject constructor() {
    suspend fun openDispute(token: String, request: OpenDisputeRequest): Dispute? {
        val response = RetrofitClient.apiService.openDispute("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun getDisputes(token: String): List<Dispute> {
        val response = RetrofitClient.apiService.getDisputes("Bearer $token")
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun getDispute(token: String, disputeId: String): Dispute? {
        val response = RetrofitClient.apiService.getDispute("Bearer $token", disputeId)
        return if (response.isSuccessful) response.body() else null
    }
}
