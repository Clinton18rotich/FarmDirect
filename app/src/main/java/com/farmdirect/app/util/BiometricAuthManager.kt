package com.farmdirect.app.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BiometricAuthManager(private val context: Context) {
    
    private val _isAvailable = MutableStateFlow(false)
    val isAvailable = _isAvailable.asStateFlow()
    
    private val _isEnabled = MutableStateFlow(false)
    val isEnabled = _isEnabled.asStateFlow()
    
    private val prefs = context.getSharedPreferences("farmdirect_prefs", Context.MODE_PRIVATE)
    
    init {
        checkAvailability()
        _isEnabled.value = prefs.getBoolean("biometric_enabled", false)
    }
    
    fun checkAvailability(): Int {
        val biometricManager = BiometricManager.from(context)
        val result = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        _isAvailable.value = result == BiometricManager.BIOMETRIC_SUCCESS
        return result
    }
    
    fun enableBiometric(enabled: Boolean) {
        _isEnabled.value = enabled
        prefs.edit().putBoolean("biometric_enabled", enabled).apply()
    }
    
    fun authenticate(
        activity: FragmentActivity,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock FarmDirect")
            .setSubtitle("Use your fingerprint or face to unlock")
            .setNegativeButtonText("Use PIN")
            .setConfirmationRequired(false)
            .build()
        
        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
            
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onError(errString.toString())
            }
            
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onFailed()
            }
        })
        
        biometricPrompt.authenticate(promptInfo)
    }
    
    fun getAvailableBiometricName(): String {
        val biometricManager = BiometricManager.from(context)
        return when {
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    "Face or Fingerprint"
                } else {
                    "Fingerprint"
                }
            }
            else -> "Not Available"
        }
    }
}
