package com.farmdirect.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.farmdirect.app.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.Preferences.NAME)

class FarmDirectPreferences(private val context: Context) {
    val authToken: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.AUTH_TOKEN] ?: "" }
    val userId: Flow<String> = context.dataStore.data.map { it[PreferencesKeys.USER_ID] ?: "" }
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[PreferencesKeys.IS_LOGGED_IN] ?: false }

    suspend fun saveAuthData(token: String, userId: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.AUTH_TOKEN] = token
            prefs[PreferencesKeys.USER_ID] = userId
            prefs[PreferencesKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }

    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey(Constants.Preferences.KEY_TOKEN)
        val USER_ID = stringPreferencesKey(Constants.Preferences.KEY_USER_ID)
        val IS_LOGGED_IN = booleanPreferencesKey(Constants.Preferences.KEY_IS_LOGGED_IN)
    }
}
