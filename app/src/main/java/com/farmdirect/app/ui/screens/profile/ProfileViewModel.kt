package com.farmdirect.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.farmdirect.app.data.local.FarmDirectPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val preferences: FarmDirectPreferences) : ViewModel() {
    fun logout() { runBlocking { preferences.clearAll() } }
}
