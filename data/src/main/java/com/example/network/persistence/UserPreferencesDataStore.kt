package com.example.network.persistence

import android.content.Context
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder
import com.example.network.data.UserPreferences
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

interface UserPreferencesDataStore {
    fun getUserPreferences(): Single<UserPreferences>
    fun saveUserPreferences(userPreferences: UserPreferences): Single<Boolean>
    fun clear(): Completable
}

class UserPreferencesDataStoreImpl@Inject constructor(
    @ApplicationContext private val context: Context
) : UserPreferencesDataStore {

    private val dataStore = RxPreferenceDataStoreBuilder(
        context,
        DATASTORE_NAME
    ).build()
    private val gson = GsonBuilder().create()
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getUserPreferences(): Single<UserPreferences> {
        return dataStore.data().firstOrError()
            .map { preferences ->
                val listString = preferences[PREFS_USER_PREFERENCES]
                if (listString.isNullOrEmpty()) {
                    UserPreferences(listOf())
                } else {
                    val userPreferences = gson.fromJson(listString, UserPreferences::class.java)
                    if (userPreferences.locationsList.isEmpty()) preferences.toMutablePreferences().clear()
                    userPreferences
                }
            }
            .onErrorReturnItem(UserPreferences(listOf()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun saveUserPreferences(userPreferences: UserPreferences): Single<Boolean> {
        val updateResult: Single<Preferences> = dataStore.updateDataAsync { prefsIn ->
            val mutablePreferences: MutablePreferences = prefsIn.toMutablePreferences()
            mutablePreferences.set(PREFS_USER_PREFERENCES, gson.toJson(userPreferences))
            Single.just(mutablePreferences)
        }
        return Single.just(updateResult.blockingGet() !== null)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun clear(): Completable {
        return dataStore.data()
            .first(null)
            .flatMapCompletable { preferences ->
                Completable.fromAction { preferences.toMutablePreferences().clear() }
            }
    }

    companion object {
        private const val DATASTORE_NAME = "user_preferences_data_store"
        private val PREFS_USER_PREFERENCES = stringPreferencesKey("prefs_user_preferences")
    }
}