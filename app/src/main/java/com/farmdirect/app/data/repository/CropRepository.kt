package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Crop
import com.farmdirect.app.domain.model.PostCropRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropRepository @Inject constructor() {
    suspend fun getCrops(token: String, category: String? = null): List<Crop> {
        val response = RetrofitClient.apiService.getCrops("Bearer $token", category)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun getCrop(token: String, cropId: String): Crop? {
        val response = RetrofitClient.apiService.getCrop("Bearer $token", cropId)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun postCrop(token: String, request: PostCropRequest): Crop? {
        val response = RetrofitClient.apiService.postCrop("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
}
