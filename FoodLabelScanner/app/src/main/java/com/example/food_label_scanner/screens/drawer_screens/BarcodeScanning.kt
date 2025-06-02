package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.food_label_scanner.barcode_functionality.*


@Composable
fun BarcodeScanning(navController: NavController) {
    var scannedBarcode by remember { mutableStateOf<String?>(null) }

    Column {
        if (scannedBarcode != null) {
            navController.navigate("barcodeDisplay/$scannedBarcode")
            scannedBarcode = null // Reset to avoid immediate re-navigation
        }
        CameraPreview(
            onBarcodeDetected = { barcode ->
                scannedBarcode = barcode
            },
            onPhotoCaptured = {}
        )
    }
}

