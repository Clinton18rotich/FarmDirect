package com.farmdirect.app.ui.screens.finance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.FinanceRepository
import com.farmdirect.app.domain.model.FinanceSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FinanceUiState(val summary: FinanceSummary? = null, val isLoading: Boolean = false)

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val financeRepository: FinanceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FinanceUiState())
    val state = _state.asStateFlow()

    init { loadSummary() }

    fun loadSummary() {
        viewModelScope.launch {
            _state.value = FinanceUiState(isLoading = true)
            val token = preferences.authToken.firstOrNull() ?: ""
            _state.value = FinanceUiState(summary = financeRepository.getFinanceSummary(token))
        }
    }
}
