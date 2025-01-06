package com.example.food_label_scanner.data

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class ImageDataStore (context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "image_uris")
    private val dataStore = context.dataStore

    suspend fun saveImageUris(uris: List<String>) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("image_uris")] = uris.joinToString(","){it.toString()}
        }
    }

    suspend fun loadImageUris(): List<String> {
        val preferences = dataStore.data.first()
        val urisString = preferences[stringPreferencesKey("image_uris")] ?: ""
        return urisString.split(",").filter { it.isNotEmpty() }
    }
}