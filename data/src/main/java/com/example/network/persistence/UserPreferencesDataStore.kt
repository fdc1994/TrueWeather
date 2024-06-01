package com.example.network.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.network.data.UserPreferences
import com.example.network.persistence.UserPreferencesDataStoreImpl.Companion.DATASTORE_NAME
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

interface UserPreferencesDataStore {
    suspend fun getUserPreferences(): UserPreferences
    suspend fun saveUserPreferences(locations: List<String>): Boolean
    suspend fun clear()
}

class UserPreferencesDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesDataStore {

    private val gson = GsonBuilder().create()

    override suspend fun getUserPreferences(): UserPreferences {
        val preferences = context.dataStore.data.map { preferences ->
            val listString = preferences[PREFS_USER_PREFERENCES]
            if (listString.isNullOrEmpty()) {
                UserPreferences(listOf())
            } else {
                val userPreferences = gson.fromJson(listString, UserPreferences::class.java)
                if (userPreferences.locationsList.isEmpty()) {
                    clear()
                    UserPreferences(listOf())
                } else {
                    userPreferences
                }
            }
        }.first()
        return preferences
    }

    override suspend fun saveUserPreferences(locations: List<String>): Boolean {
        context.dataStore.edit { preferences ->
            preferences[PREFS_USER_PREFERENCES] = gson.toJson(UserPreferences(locations))
        }
        return true
    }

    override suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        const val DATASTORE_NAME = "user_preferences_data_store"
        private val PREFS_USER_PREFERENCES = stringPreferencesKey("prefs_user_preferences")
    }
}