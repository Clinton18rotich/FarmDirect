package com.farmdirect.app.ui.screens.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.VideoRepository
import com.farmdirect.app.domain.model.VideoContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: VideoRepository) : ViewModel() {
    private val _videos = MutableStateFlow<List<VideoContent>>(emptyList())
    val videos = _videos.asStateFlow()

    fun loadFeed() {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; _videos.value = repository.getVideoFeed(token) }
    }

    fun likeVideo(videoId: String) {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; repository.likeVideo(token, videoId) }
    }
}
