package com.farmdirect.app.data.ml

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

class ModelCache(private val context: Context) {
    
    private val cacheDir: File
        get() = File(context.cacheDir, "ai_models").also { it.mkdirs() }
    
    /**
     * Cache prediction results for faster repeated queries
     */
    fun cachePrediction(
        key: String,
        value: Double,
        ttlMs: Long = 30 * 60 * 1000 // 30 minutes default
    ) {
        val file = File(cacheDir, "$key.properties")
        Properties().apply {
            setProperty("value", value.toString())
            setProperty("timestamp", System.currentTimeMillis().toString())
            setProperty("ttl", ttlMs.toString())
        }.also {
            FileOutputStream(file).use { out -> it.store(out, null) }
        }
    }
    
    /**
     * Get cached prediction if still valid
     */
    fun getCachedPrediction(key: String): Double? {
        val file = File(cacheDir, "$key.properties")
        if (!file.exists()) return null
        
        val props = Properties()
        FileInputStream(file).use { props.load(it) }
        
        val timestamp = props.getProperty("timestamp")?.toLongOrNull() ?: 0
        val ttl = props.getProperty("ttl")?.toLongOrNull() ?: 0
        val value = props.getProperty("value")?.toDoubleOrNull() ?: return null
        
        return if (System.currentTimeMillis() - timestamp < ttl) value else {
            file.delete(); null
        }
    }
    
    /**
     * Clear all cached predictions
     */
    fun clearCache() {
        cacheDir.listFiles()?.forEach { it.delete() }
    }
    
    /**
     * Get cache stats
     */
    fun getCacheStats(): CacheStats {
        val files = cacheDir.listFiles() ?: emptyArray()
        val valid = files.count { !isExpired(it) }
        val total = files.size
        return CacheStats(totalCached = total, validCache = valid, expiredCache = total - valid)
    }
    
    private fun isExpired(file: File): Boolean {
        val props = Properties()
        FileInputStream(file).use { props.load(it) }
        val timestamp = props.getProperty("timestamp")?.toLongOrNull() ?: 0
        val ttl = props.getProperty("ttl")?.toLongOrNull() ?: 0
        return System.currentTimeMillis() - timestamp > ttl
    }
    
    data class CacheStats(
        val totalCached: Int,
        val validCache: Int,
        val expiredCache: Int
    )
}
