package com.example.food_label_scanner.ui_elements

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.AsyncImage

@Composable
fun DisplayImagePreview(
    modifier: Modifier = Modifier,
    selectedImage: Uri?,
    detectedText: String?,
    onImageClick: () -> Unit
) {
    if (selectedImage != null) {
        Column(
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            // Show the selected image
            AsyncImage(
                model = selectedImage,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .clickable { onImageClick() } // Remove image on click
                    .fillMaxWidth(), // Ensure image takes full width
                contentScale = ContentScale.Crop
            )

            // Display the detected text underneath the image
            if (!detectedText.isNullOrBlank()) {
                Text(
                    text = detectedText,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            }
        }
    }
}