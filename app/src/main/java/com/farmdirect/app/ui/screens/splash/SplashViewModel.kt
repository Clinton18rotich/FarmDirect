package com.farmdirect.app.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.farmdirect.app.data.local.FarmDirectPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val preferences: FarmDirectPreferences) : ViewModel() {
    fun isLoggedIn(): Boolean = runBlocking { preferences.isLoggedIn.firstOrNull() ?: false }
}
