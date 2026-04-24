package com.farmdirect.app.ui.screens.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.BlockchainRepository
import com.farmdirect.app.domain.model.BlockchainRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: BlockchainRepository) : ViewModel() {
    private val _records = MutableStateFlow<List<BlockchainRecord>>(emptyList())
    val records = _records.asStateFlow()

    fun loadRecords() {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; val userId = preferences.userId.firstOrNull() ?: ""; _records.value = repository.getRecords(token, userId) }
    }
}
