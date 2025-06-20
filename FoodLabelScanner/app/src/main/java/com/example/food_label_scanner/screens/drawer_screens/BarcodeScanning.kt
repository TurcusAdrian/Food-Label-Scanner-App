package com.example.food_label_scanner.screens.drawer_screens


import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
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
            }
        )
    }
}

