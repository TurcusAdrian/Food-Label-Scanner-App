package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.mlkit.vision.barcode.common.Barcode
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.food_label_scanner.barcode_functionality.CameraPreview

@Composable
fun BarcodeScanning() {
    var scannedBarcode by remember { mutableStateOf<String?>(null) }

    Column {
        if (scannedBarcode != null) {
            AlertDialog(
                onDismissRequest = { scannedBarcode = null },
                title = { Text("Scanned Barcode") },
                text = { Text("Barcode: $scannedBarcode") },
                confirmButton = {
                    Button(onClick = { scannedBarcode = null }) {
                        Text("OK")
                    }
                }
            )
        }
        CameraPreview(
            onBarcodeDetected = { barcode ->
                scannedBarcode = barcode
            },
            onPhotoCaptured = {}
        )
    }
}