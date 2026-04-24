package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeliveryRepository @Inject constructor() {
    suspend fun requestDelivery(token: String, request: RequestDeliveryRequest): Delivery? {
        val response = RetrofitClient.apiService.requestDelivery("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun getDelivery(token: String, deliveryId: String): Delivery? {
        val response = RetrofitClient.apiService.getDelivery("Bearer $token", deliveryId)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun registerRider(token: String, request: RiderRegistrationRequest): Rider? {
        val response = RetrofitClient.apiService.registerRider("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
}
