package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LiveLocationSharing(private val context: Context) {
    
    private val _currentLocation = MutableStateFlow<LocationData?>(null)
    val currentLocation = _currentLocation.asStateFlow()
    
    private val _isSharing = MutableStateFlow(false)
    val isSharing = _isSharing.asStateFlow()
    
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    
    data class LocationData(
        val latitude: Double,
        val longitude: Double,
        val accuracy: Float,
        val speed: Float = 0f,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun getCurrentLocation(onLocation: (LocationData?) -> Unit) {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val data = LocationData(it.latitude, it.longitude, it.accuracy, it.speed)
                    _currentLocation.value = data
                    onLocation(data)
                } ?: onLocation(null)
            }
        } catch (e: SecurityException) {
            onLocation(null)
        }
    }
    
    fun startSharing() {
        _isSharing.value = true
    }
    
    fun stopSharing() {
        _isSharing.value = false
    }
    
    fun shareLocation(recipientPhone: String?, location: LocationData) {
        val mapsUrl = "https://maps.google.com/?q=${location.latitude},${location.longitude}"
        val message = """
            📍 My Location
            ${location.latitude}, ${location.longitude}
            Accuracy: ${"%.0f".format(location.accuracy)}m
            
            View on map: $mapsUrl
        """.trimIndent()
        
        if (recipientPhone != null) {
            // Send via SMS
            SMSBridge.sendSMS(recipientPhone, message)
        } else {
            // Share via any app
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            context.startActivity(Intent.createChooser(intent, "Share Location"))
        }
    }
    
    fun openInMaps(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
    
    fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
}
