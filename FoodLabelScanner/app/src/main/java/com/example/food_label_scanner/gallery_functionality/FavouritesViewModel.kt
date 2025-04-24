package com.example.food_label_scanner.gallery_functionality

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.path
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import java.io.File

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _images = mutableStateOf<List<Uri>>(emptyList())
    val images: State<List<Uri>> = _images

    fun saveImageToAppStorage(context: Context, sourceUri: Uri, fileName: String): Uri? {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(sourceUri)
            val targetFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/FoodLabelScanner"), fileName)
            inputStream?.use { input ->
                targetFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return Uri.fromFile(targetFile)
        } catch (e: Exception) {
            Log.e("SaveImage", "Error saving image to app storage", e)
            return null
        }
    }

    fun addImage(uri: Uri) {
        viewModelScope.launch {
            val savedUri = saveImageToAppStorage(context, uri, "image_${System.currentTimeMillis()}.jpg")
            if (savedUri != null) {
                _images.value = _images.value + savedUri
                val imageDataStore = ImageDataStoreManager.imageDataStore
                val uriStrings = _images.value.map { it.toString() }
                imageDataStore.saveImageUris(uriStrings)
            } else {
                Log.e("AddImage", "Failed to save image locally")
            }
        }
    }

    fun loadImages() {
        viewModelScope.launch {
            val imageDataStore = ImageDataStoreManager.imageDataStore
            val savedUris = imageDataStore.loadImageUris()
            val validUris = savedUris.mapNotNull { uriString ->
                val uri = Uri.parse(uriString)
                if (File(uri.path ?: "").exists()) uri else null
            }
            _images.value = validUris
            Log.d("LoadImages", "Loaded ${_images.value.size} valid images")
        }
    }

    fun deleteImage(image: Uri) {
        viewModelScope.launch {
            try {
                val filePath = getRealPathFromUri(image)
                if (filePath != null) {
                    val file = File(filePath)
                    if (file.exists()) {
                        if (file.delete()) {
                            Log.d("FavouritesViewModel", "File deleted successfully: $filePath")
                            // Update the list of images after successful deletion
                            _images.value = _images.value.filter { it != image }
                            // Delete the URI from the data store
                            val imageDataStore = ImageDataStoreManager.imageDataStore
                            imageDataStore.deleteImageUri(image.toString())
                        } else {
                            Log.e("FavouritesViewModel", "Failed to delete file: $filePath")
                        }
                    } else {
                        Log.w("FavouritesViewModel", "File does not exist: $filePath")
                    }
                } else {
                    Log.e("FavouritesViewModel", "Failed to get real path from URI: $image")
                }
            } catch (e: Exception) {
                Log.e("FavouritesViewModel", "Error deleting file", e)
            }
        }
    }

    fun removeImage(image: Uri) {
        _images.value = _images.value.filter { it != image }
        viewModelScope.launch {
            val imageDataStore = ImageDataStoreManager.imageDataStore
            val uriStrings = _images.value.map { it.toString() }
            imageDataStore.saveImageUris(uriStrings)
        }
    }


    fun getRealPathFromUri(uri: Uri): String?{
        return if (uri.scheme == ContentResolver.SCHEME_FILE){
            uri.path
        } else{
            null
        }
    }
    fun downloadImage(image: Uri) {
        Log.d("Download Image function", "Downloading image ... success!")
    }
}