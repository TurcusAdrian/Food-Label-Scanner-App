package com.example.food_label_scanner.camera_functionality

import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePhotoToGallery: SavePhotoToGallery
) : ViewModel(){

    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage = _capturedImage.asStateFlow()

    fun storePhotoInGallery(bitmap: Bitmap){
        viewModelScope.launch{
            savePhotoToGallery.call(bitmap)
            _capturedImage.value?.recycle()
            _capturedImage.value = bitmap
        }
    }

    override fun onCleared() {
        _capturedImage.value?.recycle()
        super.onCleared()
    }

}