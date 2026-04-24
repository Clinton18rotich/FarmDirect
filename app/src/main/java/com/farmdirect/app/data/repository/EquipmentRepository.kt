package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Equipment
import com.farmdirect.app.domain.model.EquipmentRental
import com.farmdirect.app.domain.model.PostEquipmentRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EquipmentRepository @Inject constructor() {
    suspend fun getEquipment(token: String, type: String? = null): List<Equipment> {
        val response = RetrofitClient.apiService.getEquipment("Bearer $token", type, null)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun postEquipment(token: String, request: PostEquipmentRequest): Equipment? {
        val response = RetrofitClient.apiService.postEquipment("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun rentEquipment(token: String, equipmentId: String, days: Int): EquipmentRental? {
        val response = RetrofitClient.apiService.rentEquipment("Bearer $token", mapOf("equipmentId" to equipmentId, "days" to days.toString()))
        return if (response.isSuccessful) response.body() else null
    }
}
