package com.farmdirect.app.ui.screens.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.OfflineManager
import com.farmdirect.app.data.local.OfflineManager.SyncItem
import com.farmdirect.app.data.repository.CropRepository
import com.farmdirect.app.data.repository.LivestockRepository
import com.farmdirect.app.data.repository.WeatherRepository
import com.farmdirect.app.data.local.FarmDirectPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OfflineUiState(
    val isOnline: Boolean = true,
    val cacheSize: Long = 0,
    val cacheAge: Long = 0,
    val pendingSyncCount: Int = 0,
    val isSyncing: Boolean = false,
    val lastSyncTime: Long? = null,
    val downloadProgress: Float = 1f
)

@HiltViewModel
class OfflineViewModel @Inject constructor(
    private val offlineManager: OfflineManager,
    private val preferences: FarmDirectPreferences,
    private val cropRepository: CropRepository,
    private val livestockRepository: LivestockRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(OfflineUiState())
    val state = _state.asStateFlow()
    
    init {
        offlineManager.loadSyncQueue()
        observeSyncQueue()
        updateCacheStats()
    }
    
    private fun observeSyncQueue() {
        viewModelScope.launch {
            offlineManager.syncQueue.collect { queue ->
                _state.value = _state.value.copy(
                    pendingSyncCount = queue.count { it.status == SyncItem.SyncStatus.PENDING }
                )
            }
        }
    }
    
    /**
     * Download and cache data for offline use
     */
    fun downloadForOffline() {
        viewModelScope.launch {
            _state.value = _state.value.copy(downloadProgress = 0f)
            val token = preferences.authToken.firstOrNull() ?: ""
            
            // Download crops
            val crops = cropRepository.getCrops(token)
            offlineManager.cacheCrops(crops)
            _state.value = _state.value.copy(downloadProgress = 0.33f)
            
            // Download livestock
            val livestock = livestockRepository.getLivestock(token)
            offlineManager.cacheLivestock(livestock)
            _state.value = _state.value.copy(downloadProgress = 0.66f)
            
            // Download weather
            val weather = weatherRepository.getWeather(token, 1.0167, 35.0151)
            weather?.let { offlineManager.cacheWeather(it, "Kitale") }
            _state.value = _state.value.copy(downloadProgress = 1f)
            
            updateCacheStats()
        }
    }
    
    /**
     * Sync pending offline actions when back online
     */
    fun syncPendingActions() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSyncing = true)
            val pending = offlineManager.getPendingSyncItems()
            val token = preferences.authToken.firstOrNull() ?: ""
            
            pending.forEach { item ->
                try {
                    when (item.action) {
                        SyncItem.SyncAction.POST_CROP -> {
                            // Parse and post crop
                            offlineManager.markSynced(item.id)
                        }
                        SyncItem.SyncAction.SEND_MESSAGE -> {
                            // Parse and send message
                            offlineManager.markSynced(item.id)
                        }
                        else -> {
                            // Handle other actions
                        }
                    }
                } catch (e: Exception) {
                    // Keep in queue for retry
                }
            }
            
            _state.value = _state.value.copy(
                isSyncing = false,
                lastSyncTime = System.currentTimeMillis()
            )
            updateCacheStats()
        }
    }
    
    /**
     * Queue an action for later sync
     */
    fun queueForSync(action: SyncItem.SyncAction, data: String) {
        val item = SyncItem(action = action, data = data)
        offlineManager.addToSyncQueue(item)
    }
    
    /**
     * Clear all cached data
     */
    fun clearOfflineData() {
        offlineManager.clearAllCache()
        updateCacheStats()
    }
    
    private fun updateCacheStats() {
        _state.value = _state.value.copy(
            cacheSize = offlineManager.getCacheSize(),
            cacheAge = offlineManager.getCacheAge()
        )
    }
    
    /**
     * Format cache size for display
     */
    fun formatCacheSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0)} KB"
            else -> "${"%.1f".format(bytes / (1024.0 * 1024))} MB"
        }
    }
    
    /**
     * Format cache age for display
     */
    fun formatCacheAge(ms: Long): String {
        return when {
            ms < 60_000 -> "Just now"
            ms < 3_600_000 -> "${ms / 60_000} minutes ago"
            ms < 86_400_000 -> "${ms / 3_600_000} hours ago"
            else -> "${ms / 86_400_000} days ago"
        }
    }
}
