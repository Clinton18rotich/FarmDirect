package com.farmdirect.app.ui.screens.carbon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.CarbonRepository
import com.farmdirect.app.domain.model.CarbonCredit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarbonViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: CarbonRepository) : ViewModel() {
    private val _credits = MutableStateFlow<CarbonCredit?>(null)
    val credits = _credits.asStateFlow()

    fun loadCredits() {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; _credits.value = repository.getCarbonCredits(token) }
    }

    fun sellCredits(amount: Double) {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; repository.sellCredits(token, amount); loadCredits() }
    }
}
