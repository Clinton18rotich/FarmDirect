package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Cooperative
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CooperativeRepository @Inject constructor() {
    suspend fun getCooperatives(token: String): List<Cooperative> {
        val response = RetrofitClient.apiService.getCooperatives("Bearer $token")
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun createCooperative(token: String, name: String, desc: String, loc: String): Cooperative? {
        val response = RetrofitClient.apiService.createCooperative("Bearer $token", mapOf("name" to name, "description" to desc, "location" to loc))
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun joinCooperative(token: String, co-opId: String): Boolean {
        return RetrofitClient.apiService.joinCooperative("Bearer $token", mapOf("cooperativeId" to co-opId)).isSuccessful
    }
}
