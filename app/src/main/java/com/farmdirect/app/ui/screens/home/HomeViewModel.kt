package com.farmdirect.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.CropRepository
import com.farmdirect.app.domain.model.Crop
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val cropRepository: CropRepository
) : ViewModel() {
    private val _crops = MutableStateFlow<List<Crop>>(emptyList())
    val crops = _crops.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory = _selectedCategory.asStateFlow()

    init { loadCrops() }

    fun loadCrops() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = preferences.authToken.firstOrNull() ?: ""
            _crops.value = cropRepository.getCrops(token, if (_selectedCategory.value == "All") null else _selectedCategory.value)
            _isLoading.value = false
        }
    }

    fun selectCategory(category: String) { _selectedCategory.value = category; loadCrops() }
}
