package com.farmdirect.app.ui.screens.ai

import android.graphics.Bitmap
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.AIRepository
import com.farmdirect.app.domain.model.DiseaseDetection
import com.farmdirect.app.domain.model.PricePrediction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

data class AIUiState(val pricePrediction: PricePrediction? = null, val diseaseDetection: DiseaseDetection? = null, val isLoading: Boolean = false)

@HiltViewModel
class AIViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: AIRepository) : ViewModel() {
    private val _state = MutableStateFlow(AIUiState())
    val state = _state.asStateFlow()

    fun predictPrice(cropType: String, lat: Double = 1.0167, lng: Double = 35.0151) {
        viewModelScope.launch { _state.value = _state.value.copy(isLoading = true); val token = preferences.authToken.firstOrNull() ?: ""; _state.value = _state.value.copy(pricePrediction = repository.predictPrice(token, cropType, lat, lng), isLoading = false) }
    }

    fun detectDisease(bitmap: Bitmap, cropType: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val stream = ByteArrayOutputStream(); bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            val base64 = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
            val token = preferences.authToken.firstOrNull() ?: ""
            _state.value = _state.value.copy(diseaseDetection = repository.detectDisease(token, cropType, base64), isLoading = false)
        }
    }
}
