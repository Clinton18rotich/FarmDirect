package com.farmdirect.app.ui.screens.livestock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.LivestockRepository
import com.farmdirect.app.domain.model.Livestock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LivestockViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: LivestockRepository) : ViewModel() {
    private val _livestock = MutableStateFlow<List<Livestock>>(emptyList())
    val livestock = _livestock.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadLivestock(type: String? = null) {
        viewModelScope.launch { _isLoading.value = true; val token = preferences.authToken.firstOrNull() ?: ""; _livestock.value = repository.getLivestock(token, type); _isLoading.value = false }
    }
}
