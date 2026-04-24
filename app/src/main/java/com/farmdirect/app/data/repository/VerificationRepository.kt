package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Verification
import com.farmdirect.app.domain.model.VerificationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VerificationRepository @Inject constructor() {
    suspend fun submitVerification(token: String, request: VerificationRequest): Verification? {
        val response = RetrofitClient.apiService.submitVerification("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun getVerificationStatus(token: String): Verification? {
        val response = RetrofitClient.apiService.getVerificationStatus("Bearer $token")
        return if (response.isSuccessful) response.body() else null
    }
}
