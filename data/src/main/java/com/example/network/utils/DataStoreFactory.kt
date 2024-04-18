package com.example.network.utils

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

object DataStoreFactory {

    /**
     * Creates and instance of DataStore<Preferences>
     * -Note: Please keep the result as a Singleton and consider to either kotlin lazy delegation or [Dagger.lazy].
     *
     * @param name DataStore name
     * @param applicationContext Android application context
     * @param coroutineContext Coroutine scope for performing IO operation
     * @return returns an instance of [DataStore]
     */
    fun createPreferences(
        name: String,
        applicationContext: Context,
        coroutineContext: CoroutineContext,
        migrations: List<DataMigration<Preferences>> = listOf()
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            scope = CoroutineScope(coroutineContext + SupervisorJob()),
            migrations = migrations,
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() } // Use an empty preferences in case of file corruption
            )
        ) {
            applicationContext.preferencesDataStoreFile(name)
        }
    }
}