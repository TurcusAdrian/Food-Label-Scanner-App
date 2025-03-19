package com.example.food_label_scanner.gallery_functionality

import android.content.Context
import android.net.Uri
import android.util.Log
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
            preferences[stringPreferencesKey("image_uris")] = uris.joinToString(",")
        }
        Log.d("Save Image URIs", "Saving image URI ... success!")
    }

    suspend fun loadImageUris(): List<String> {
        val preferences = dataStore.data.first()
        val urisString = preferences[stringPreferencesKey("image_uris")] ?: ""
        Log.d("Load Image URIs", "Loading image URI ... success!")
        return urisString.split(",").filter { it.isNotEmpty() }
    }


    suspend fun deleteImageUri(uriToDelete: String) {
        dataStore.edit { preferences ->
            val currentUrisString = preferences[stringPreferencesKey("image_uris")] ?: ""
            val currentUris = currentUrisString.split(",").filter { it.isNotEmpty() }.toMutableList()

            if (currentUris.remove(uriToDelete)) {
                preferences[stringPreferencesKey("image_uris")] = currentUris.joinToString(",")
                Log.d("Delete Image URI", "Deleted image URI: $uriToDelete")
            } else {
                Log.w("Delete Image URI", "Image URI not found: $uriToDelete")
            }
        }
    }
}