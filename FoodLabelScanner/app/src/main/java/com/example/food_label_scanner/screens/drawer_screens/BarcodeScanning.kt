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
/*
@Composable
fun BarcodeScanning(productViewModel: ProductViewModel = hiltViewModel()) {
    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var showCamera by remember { mutableStateOf(true) }
    val product by productViewModel.product.observeAsState()
    val isLoading by productViewModel.isLoading.observeAsState(false)
    val error by productViewModel.error.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            CameraPreview(
                onBarcodeDetected = { barcode ->
                    scannedBarcode = barcode

                },
                onPhotoCaptured = {}
            )
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(text = "Error: $error")
            LaunchedEffect(key1 = error) {
                showCamera = true
                scannedBarcode = null
            }
        } else if (product != null) {
            DisplayProductDetails(product = product!!)
            LaunchedEffect(key1 = product) {
                showCamera = false
                scannedBarcode = null
            }
        }
        LaunchedEffect(key1 = scannedBarcode) {
            if (scannedBarcode != null) {
                productViewModel.fetchProduct(scannedBarcode!!)
            }
        }
    }
}
*/
@Composable
fun DisplayProductDetails(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (product.imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(model = product.imageUrl),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (product.productName != null) {
            Text(text = product.productName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (product.brands != null) {
            Text(text = "Brand: ${product.brands}")
        }
        if (product.categories != null) {
            Text(text = "Categories: ${product.categories}")
        }
        if (product.countries != null) {
            Text(text = "Countries: ${product.countries}")
        }
        if (product.allergens != null) {
            Text(text = "Allergens: ${product.allergens}")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (product.ingredients != null) {
            Text(text = "Ingredients:", fontWeight = FontWeight.Bold)
            Text(text = product.ingredients)
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (product.nutriments != null) {
            Text(text = "Nutriments:", fontWeight = FontWeight.Bold)
            product.nutriments.apply {
                if (energyKcal100g != null) {
                    Text(text = "Energy (kcal/100g): $energyKcal100g")
                }
                if (fat100g != null) {
                    Text(text = "Fat (g/100g): $fat100g")
                }
                if (saturatedFat100g != null) {
                    Text(text = "Saturated Fat (g/100g): $saturatedFat100g")
                }
                if (carbohydrates100g != null) {
                    Text(text = "Carbohydrates (g/100g): $carbohydrates100g")
                }
                if (sugars100g != null) {
                    Text(text = "Sugars (g/100g): $sugars100g")
                }
                if (proteins100g != null) {
                    Text(text = "Proteins (g/100g): $proteins100g")
                }
                if (salt100g != null) {
                    Text(text = "Salt (g/100g): $salt100g")
                }
            }
        }
    }
}