package com.farmdirect.app.ui.screens.crops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.CropRepository
import com.farmdirect.app.domain.model.Crop
import com.farmdirect.app.domain.model.PostCropRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CropDetailUiState(val crop: Crop? = null, val isLoading: Boolean = false, val error: String? = null)
data class PostCropUiState(val isPosting: Boolean = false, val isSuccess: Boolean = false, val error: String? = null)

@HiltViewModel
class CropViewModel @Inject constructor(
    private val preferences: FarmDirectPreferences,
    private val cropRepository: CropRepository
) : ViewModel() {
    private val _cropDetailState = MutableStateFlow(CropDetailUiState())
    val cropDetailState = _cropDetailState.asStateFlow()
    private val _postCropState = MutableStateFlow(PostCropUiState())
    val postCropState = _postCropState.asStateFlow()

    fun loadCropDetail(cropId: String) {
        viewModelScope.launch {
            _cropDetailState.value = CropDetailUiState(isLoading = true)
            val token = preferences.authToken.firstOrNull() ?: ""
            val crop = cropRepository.getCrop(token, cropId)
            _cropDetailState.value = if (crop != null) CropDetailUiState(crop = crop) else CropDetailUiState(error = "Not found")
        }
    }

    fun postCrop(request: PostCropRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _postCropState.value = PostCropUiState(isPosting = true)
            val token = preferences.authToken.firstOrNull() ?: ""
            val result = cropRepository.postCrop(token, request)
            if (result != null) { _postCropState.value = PostCropUiState(isSuccess = true); onSuccess() }
            else _postCropState.value = PostCropUiState(error = "Failed")
        }
    }
}
