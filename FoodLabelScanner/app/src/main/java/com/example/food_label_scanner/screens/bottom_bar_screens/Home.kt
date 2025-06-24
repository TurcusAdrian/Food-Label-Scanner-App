package com.example.food_label_scanner.screens.bottom_bar_screens

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.food_label_scanner.screens.CameraCaptureContent
import com.example.food_label_scanner.screens.SharedTextViewModel
import com.example.food_label_scanner.ui_elements.DisplayImagePreview

@Composable
fun Home(
    selectedImage: Uri?,
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
            onDetectedTextUpdated = { updatedText ->
                onTextUpdated(updatedText)
            },
            onPhotoCaptured = { },
            sharedTextViewModel = sharedTextViewModel,
            navigationController = navigationController
        )
    }
}