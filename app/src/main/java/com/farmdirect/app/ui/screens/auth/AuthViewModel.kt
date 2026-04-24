package com.farmdirect.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.LoginRequest
import com.farmdirect.app.domain.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(val isLoading: Boolean = false, val error: String? = null)

@HiltViewModel
class AuthViewModel @Inject constructor(private val preferences: FarmDirectPreferences) : ViewModel() {
    private val _loginState = MutableStateFlow(AuthUiState())
    val loginState = _loginState.asStateFlow()

    fun login(phone: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginState.value = AuthUiState(true)
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(phone, password))
                if (response.isSuccessful) {
                    response.body()?.let { preferences.saveAuthData(it.token, it.userId); onSuccess() }
                } else _loginState.value = AuthUiState(error = "Invalid credentials")
            } catch (e: Exception) { _loginState.value = AuthUiState(error = e.message) }
        }
    }

    fun register(name: String, phone: String, password: String, type: String, county: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loginState.value = AuthUiState(true)
            try {
                val response = RetrofitClient.apiService.register(RegisterRequest(name, phone, password, type, county))
                if (response.isSuccessful) {
                    response.body()?.let { preferences.saveAuthData(it.token, it.userId); onSuccess() }
                } else _loginState.value = AuthUiState(error = "Registration failed")
            } catch (e: Exception) { _loginState.value = AuthUiState(error = e.message) }
        }
    }
}
