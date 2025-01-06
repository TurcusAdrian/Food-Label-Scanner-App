package com.example.food_label_scanner.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import javax.inject.Inject
import kotlin.collections.map


class FavouritesViewModel : ViewModel() {
    private val _images = mutableStateOf<List<Uri>>(emptyList())
    val images: State<List<Uri>> = _images

    fun loadImages() {
        viewModelScope.launch {
            val imageDataStore = ImageDataStoreManager.imageDataStore
            val savedUris = imageDataStore.loadImageUris()
            _images.value = savedUris.map { Uri.parse(it) }
            Log.d("Load Image function", "Loading image ... success!")
        }
    }

    fun addImage(uri: Uri) {
        _images.value = _images.value + uri
        viewModelScope.launch {
            val imageDataStore = ImageDataStoreManager.imageDataStore
            val uriStrings = _images.value.map { it.toString() }
            imageDataStore.saveImageUris(uriStrings)
        }
    }

    fun deleteImage(image: Uri) {
        _images.value = _images.value.filter { it != image }
        viewModelScope.launch {
            val imageDataStore = ImageDataStoreManager.imageDataStore
            val uriStrings = _images.value.map { it.toString() }
            imageDataStore.saveImageUris(uriStrings)
        }
    }

    fun downloadImage(image: Uri) {
        Log.d("Download Image function", "Downloading image ... success!")
    }
}