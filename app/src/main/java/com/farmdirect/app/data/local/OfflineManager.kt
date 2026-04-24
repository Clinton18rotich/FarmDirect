package com.farmdirect.app.data.local

import android.content.Context
import com.farmdirect.app.domain.model.Crop
import com.farmdirect.app.domain.model.Livestock
import com.farmdirect.app.domain.model.WeatherForecast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File

class OfflineManager(private val context: Context) {
    
    private val gson = Gson()
    private val offlineDir: File
        get() = File(context.filesDir, "offline_data").also { it.mkdirs() }
    
    private val _isOnline = MutableStateFlow(true)
    val isOnline = _isOnline.asStateFlow()
    
    private val _syncQueue = MutableStateFlow<List<SyncItem>>(emptyList())
    val syncQueue = _syncQueue.asStateFlow()
    
    // ═══════════════════════════════════════
    // CROP CACHING
    // ═══════════════════════════════════════
    
    fun cacheCrops(crops: List<Crop>) {
        val file = File(offlineDir, "crops_cache.json")
        file.writeText(gson.toJson(crops))
    }
    
    fun getCachedCrops(): List<Crop> {
        val file = File(offlineDir, "crops_cache.json")
        if (!file.exists()) return emptyList()
        val type = object : TypeToken<List<Crop>>() {}.type
        return try { gson.fromJson(file.readText(), type) ?: emptyList() } catch (e: Exception) { emptyList() }
    }
    
    // ═══════════════════════════════════════
    // LIVESTOCK CACHING
    // ═══════════════════════════════════════
    
    fun cacheLivestock(livestock: List<Livestock>) {
        val file = File(offlineDir, "livestock_cache.json")
        file.writeText(gson.toJson(livestock))
    }
    
    fun getCachedLivestock(): List<Livestock> {
        val file = File(offlineDir, "livestock_cache.json")
        if (!file.exists()) return emptyList()
        val type = object : TypeToken<List<Livestock>>() {}.type
        return try { gson.fromJson(file.readText(), type) ?: emptyList() } catch (e: Exception) { emptyList() }
    }
    
    // ═══════════════════════════════════════
    // WEATHER CACHING
    // ═══════════════════════════════════════
    
    fun cacheWeather(weather: WeatherForecast, location: String) {
        val file = File(offlineDir, "weather_${location}.json")
        file.writeText(gson.toJson(weather))
    }
    
    fun getCachedWeather(location: String): WeatherForecast? {
        val file = File(offlineDir, "weather_${location}.json")
        if (!file.exists()) return null
        return try { gson.fromJson(file.readText(), WeatherForecast::class.java) } catch (e: Exception) { null }
    }
    
    // ═══════════════════════════════════════
    // PRICE CACHING
    // ═══════════════════════════════════════
    
    fun cachePrices(prices: Map<String, Double>) {
        val file = File(offlineDir, "prices_cache.json")
        file.writeText(gson.toJson(prices))
    }
    
    fun getCachedPrices(): Map<String, Double> {
        val file = File(offlineDir, "prices_cache.json")
        if (!file.exists()) return emptyMap()
        val type = object : TypeToken<Map<String, Double>>() {}.type
        return try { gson.fromJson(file.readText(), type) ?: emptyMap() } catch (e: Exception) { emptyMap() }
    }
    
    // ═══════════════════════════════════════
    // SYNC QUEUE (Actions to perform when online)
    // ═══════════════════════════════════════
    
    fun addToSyncQueue(item: SyncItem) {
        val current = _syncQueue.value.toMutableList()
        current.add(item)
        _syncQueue.value = current
        saveSyncQueue()
    }
    
    fun removeFromSyncQueue(itemId: String) {
        val current = _syncQueue.value.toMutableList()
        current.removeAll { it.id == itemId }
        _syncQueue.value = current
        saveSyncQueue()
    }
    
    fun getPendingSyncItems(): List<SyncItem> {
        return _syncQueue.value.filter { it.status == SyncStatus.PENDING }
    }
    
    fun markSynced(itemId: String) {
        val current = _syncQueue.value.toMutableList()
        current.find { it.id == itemId }?.let {
            val index = current.indexOf(it)
            current[index] = it.copy(status = SyncStatus.SYNCED)
        }
        _syncQueue.value = current
        saveSyncQueue()
    }
    
    private fun saveSyncQueue() {
        val file = File(offlineDir, "sync_queue.json")
        file.writeText(gson.toJson(_syncQueue.value))
    }
    
    fun loadSyncQueue() {
        val file = File(offlineDir, "sync_queue.json")
        if (!file.exists()) return
        val type = object : TypeToken<List<SyncItem>>() {}.type
        try {
            _syncQueue.value = gson.fromJson(file.readText(), type) ?: emptyList()
        } catch (e: Exception) { }
    }
    
    // ═══════════════════════════════════════
    // CACHE MANAGEMENT
    // ═══════════════════════════════════════
    
    fun getCacheSize(): Long {
        return offlineDir.walkTopDown().sumOf { it.length() }
    }
    
    fun getCacheAge(): Long {
        val files = offlineDir.listFiles() ?: return 0
        if (files.isEmpty()) return 0
        val oldest = files.minOf { it.lastModified() }
        return System.currentTimeMillis() - oldest
    }
    
    fun clearOldCache(maxAgeMs: Long = 7 * 24 * 60 * 60 * 1000) {
        val cutoff = System.currentTimeMillis() - maxAgeMs
        offlineDir.listFiles()?.forEach { file ->
            if (file.lastModified() < cutoff) file.delete()
        }
    }
    
    fun clearAllCache() {
        offlineDir.listFiles()?.forEach { it.delete() }
    }
    
    fun setOnlineStatus(online: Boolean) {
        _isOnline.value = online
    }
    
    data class SyncItem(
        val id: String = java.util.UUID.randomUUID().toString(),
        val action: SyncAction,
        val data: String,          // JSON serialized data
        val status: SyncStatus = SyncStatus.PENDING,
        val createdAt: Long = System.currentTimeMillis()
    )
    
    enum class SyncAction {
        POST_CROP,
        POST_LIVESTOCK,
        PLACE_ORDER,
        SEND_MESSAGE,
        SUBMIT_REVIEW,
        REPORT_ISSUE
    }
    
    enum class SyncStatus {
        PENDING, SYNCING, SYNCED, FAILED
    }
}
