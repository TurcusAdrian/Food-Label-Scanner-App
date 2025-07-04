package com.example.food_label_scanner.gallery_functionality

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.userImageDataStore: DataStore<Preferences> by preferencesDataStore(name = "image_uris")


class ImageDataStore (private val context: Context) {

    private val dataStore = context.userImageDataStore

    private fun getUserId(): Int {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("userId", -1)
    }

    private fun getKey(): String {
        val userId = getUserId()
        return "image_uris_user_$userId"
    }

    suspend fun saveImageUris(uris: List<String>) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(getKey())] = uris.joinToString(",")
        }
        Log.d("Save Image URIs", "Saving image URIs for user ${getUserId()} ... success!")
    }

    suspend fun loadImageUris(): List<String> {
        val preferences = dataStore.data.first()
        val urisString = preferences[stringPreferencesKey(getKey())] ?: ""
        Log.d("Load Image URIs", "Loading image URIs for user ${getUserId()} ... success!")
        return urisString.split(",").filter { it.isNotEmpty() }
    }

}