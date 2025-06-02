package com.example.food_label_scanner.screens.bottom_bar_screens

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.food_label_scanner.camera_functionality.CameraViewModel
import com.example.food_label_scanner.screens.CameraCaptureContent
import com.example.food_label_scanner.screens.SharedTextViewModel
import com.example.food_label_scanner.text_recognition.TextRecognitionAnalyzer
import com.example.food_label_scanner.ui_elements.CameraContent
import com.example.food_label_scanner.ui_elements.DisplayImagePreview

@Composable
fun Home(
    selectedImage: Uri?, // Now a nullable Uri, not MutableState
    detectedText: String,
    onTextUpdated: (String) -> Unit,
    sharedTextViewModel: SharedTextViewModel,
    navigationController: NavController,
    onClearSelectedImage: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedImage != null) {
        DisplayImagePreview(
            selectedImage = selectedImage,
            detectedText = detectedText,
            onImageClick = { onClearSelectedImage() },
            modifier = modifier.fillMaxSize()
        )
    } else {
        CameraCaptureContent(
            detectedText = detectedText,
            onDetectedTextUpdated = { updatedText ->
                onTextUpdated(updatedText)
            },
            onPhotoCaptured = { /* No-op or logging, since not used */ },
            sharedTextViewModel = sharedTextViewModel,
            navigationController = navigationController
        )
    }
}