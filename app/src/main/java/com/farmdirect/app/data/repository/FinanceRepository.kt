package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.FinanceSummary
import com.farmdirect.app.domain.model.FarmLoan
import com.farmdirect.app.domain.model.LoanApplicationRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepository @Inject constructor() {
    suspend fun getFinanceSummary(token: String): FinanceSummary? {
        val response = RetrofitClient.apiService.getFinanceSummary("Bearer $token")
        return if (response.isSuccessful) response.body() else null
    }
}
