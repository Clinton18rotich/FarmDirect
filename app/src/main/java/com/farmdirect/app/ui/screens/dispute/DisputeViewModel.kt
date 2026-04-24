package com.farmdirect.app.ui.screens.dispute

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.DisputeRepository
import com.farmdirect.app.domain.model.Dispute
import com.farmdirect.app.domain.model.OpenDisputeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisputeViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: DisputeRepository) : ViewModel() {
    private val _disputes = MutableStateFlow<List<Dispute>>(emptyList())
    val disputes = _disputes.asStateFlow()

    fun loadDisputes() {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; _disputes.value = repository.getDisputes(token) }
    }

    fun openDispute(orderId: String, type: String, reason: String, desc: String, onSuccess: () -> Unit) {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; if (repository.openDispute(token, OpenDisputeRequest(orderId, type, reason, desc, "RESOLUTION")) != null) onSuccess() }
    }
}
