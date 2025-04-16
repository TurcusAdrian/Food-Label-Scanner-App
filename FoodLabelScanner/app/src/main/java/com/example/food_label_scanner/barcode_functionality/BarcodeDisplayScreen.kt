package com.example.food_label_scanner.barcode_functionality

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@Composable
fun BarcodeDisplayScreen(barcode: String) {
    var productName by remember { mutableStateOf("Loading...") }
    var ingredients by remember { mutableStateOf("Loading...") }
    var brand by remember { mutableStateOf("Loading...") }

    LaunchedEffect(barcode) {
        fetchProduct(barcode) { name, ing, brnd ->
            productName = name ?: "Product not found"
            ingredients = ing ?: "Ingredients not found"
            brand = brnd ?: "Brand not found"
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Barcode: $barcode",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Product: $productName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Brand: $brand",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ingredients: $ingredients",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun fetchProduct(
    barcode: String,
    onResult: (String?, String?, String?) -> Unit
) {
    val client = OkHttpClient()
    val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json"
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            Log.e("API_CALL", "Error fetching product: ${e.message}", e)
            onResult(null, null, null)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val json = JSONObject(responseBody)
                    if (json.has("product")) {
                        val product = json.getJSONObject("product")
                        val name = product.optString("product_name", null)
                        val ing = product.optString("ingredients_text", null)
                        val brnd = product.optString("brands", null)
                        onResult(name, ing, brnd)
                    } else {
                        onResult(null, null, null)
                    }
                } catch (e: Exception) {
                    Log.e("API_CALL", "Error parsing JSON: ${e.message}", e)
                    onResult(null, null, null)
                }
            } else {
                Log.e("API_CALL", "Unsuccessful response: ${response.code}")
                onResult(null, null, null)
            }
        }
    })
}