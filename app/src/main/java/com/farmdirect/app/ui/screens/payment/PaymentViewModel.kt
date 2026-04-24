package com.farmdirect.app.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.PaymentRepository
import com.farmdirect.app.domain.model.MpesaPaymentRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentUiState(val isLoading: Boolean = false, val isSuccess: Boolean = false, val error: String? = null)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val paymentRepository: PaymentRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PaymentUiState())
    val state = _state.asStateFlow()

    fun initiatePayment(phone: String, amount: Double, orderId: String) {
        viewModelScope.launch {
            _state.value = PaymentUiState(isLoading = true)
            val token = preferences.authToken.firstOrNull() ?: ""
            val result = paymentRepository.initiateMpesaPayment(token, MpesaPaymentRequest(phone, amount, orderId, "FarmDirect Order"))
            if (result != null) _state.value = PaymentUiState(isSuccess = true)
            else _state.value = PaymentUiState(error = "Payment failed")
        }
    }
}
