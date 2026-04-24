package com.farmdirect.app.ui.screens.delivery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.DeliveryRepository
import com.farmdirect.app.domain.model.Delivery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeliveryViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: DeliveryRepository) : ViewModel() {
    private val _delivery = MutableStateFlow<Delivery?>(null)
    val delivery = _delivery.asStateFlow()

    fun loadDelivery(deliveryId: String) {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; _delivery.value = repository.getDelivery(token, deliveryId) }
    }
}
