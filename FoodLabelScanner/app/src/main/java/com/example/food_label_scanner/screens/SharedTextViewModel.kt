package com.example.food_label_scanner.screens

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedTextViewModel : ViewModel() {
    private val _detectedTextState = MutableStateFlow<String?>(null)
    val detectedTextState: StateFlow<String?> = _detectedTextState

    @OptIn(UnstableApi::class)
    fun setDetectedText(text: String) {
        _detectedTextState.value = text
        Log.d("SharedVM", "Text set: $text")
    }

    @OptIn(UnstableApi::class)
    fun clearDetectedText() {
        _detectedTextState.value = null
        Log.d("SharedVM", "Text cleared")
    }
}