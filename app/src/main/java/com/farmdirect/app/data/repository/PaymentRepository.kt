package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.MpesaPaymentRequest
import com.farmdirect.app.domain.model.Payment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor() {
    suspend fun initiateMpesaPayment(token: String, request: MpesaPaymentRequest): Payment? {
        val response = RetrofitClient.apiService.initiateMpesaPayment("Bearer $token", request)
        return if (response.isSuccessful) response.body() else null
    }
}
