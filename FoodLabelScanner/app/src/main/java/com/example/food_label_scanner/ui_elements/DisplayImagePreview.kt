package com.example.food_label_scanner.ui_elements

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale

import coil.compose.AsyncImage

@Composable
fun DisplayImagePreview(modifier: Modifier = Modifier, selectedImage : Uri?, onImageClick: () -> Unit){
    if (selectedImage != null) {
        // Show the selected image if available
        AsyncImage(
            model = selectedImage,
            contentDescription = "Selected Image",
            modifier = modifier.clickable { onImageClick() }, //remove image on click
            contentScale = ContentScale.Crop
        )
    }
}