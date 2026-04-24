package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.CarbonCredit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarbonRepository @Inject constructor() {
    suspend fun getCarbonCredits(token: String): CarbonCredit? {
        val response = RetrofitClient.apiService.getCarbonCredits("Bearer $token")
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun registerPractice(token: String, type: String, desc: String): Boolean {
        return RetrofitClient.apiService.registerPractice("Bearer $token", mapOf("type" to type, "description" to desc)).isSuccessful
    }
    suspend fun sellCredits(token: String, amount: Double): Boolean {
        return RetrofitClient.apiService.sellCredits("Bearer $token", mapOf("amount" to amount.toString())).isSuccessful
    }
}
