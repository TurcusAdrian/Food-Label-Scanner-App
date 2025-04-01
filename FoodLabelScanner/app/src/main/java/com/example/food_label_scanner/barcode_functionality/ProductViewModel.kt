package com.example.food_label_scanner.barcode_functionality

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class ProductViewModel : ViewModel() {

    private val _product = MutableLiveData<Product?>(null)
    val product: LiveData<Product?> = _product

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://world.openfoodfacts.org/api/v2/product/$barcode.json")
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val jsonObject = JSONObject(responseBody)
                        val status = jsonObject.getInt("status")
                        if (status == 1) {
                            val productJson = jsonObject.getJSONObject("product")
                            val nutrimentsJson = productJson.optJSONObject("nutriments")
                            val nutriments = if (nutrimentsJson != null) {
                                Nutriments(
                                    energyKcal100g = nutrimentsJson.optDouble("energy-kcal_100g"),
                                    fat100g = nutrimentsJson.optDouble("fat_100g"),
                                    saturatedFat100g = nutrimentsJson.optDouble("saturated-fat_100g"),
                                    carbohydrates100g = nutrimentsJson.optDouble("carbohydrates_100g"),
                                    sugars100g = nutrimentsJson.optDouble("sugars_100g"),
                                    proteins100g = nutrimentsJson.optDouble("proteins_100g"),
                                    salt100g = nutrimentsJson.optDouble("salt_100g")
                                )
                            } else {
                                null
                            }
                            val product = Product(
                                productName = productJson.optString("product_name"),
                                ingredients = productJson.optString("ingredients_text"),
                                imageUrl = productJson.optString("image_url"),
                                brands = productJson.optString("brands"),
                                categories = productJson.optString("categories"),
                                countries = productJson.optString("countries"),
                                allergens = productJson.optString("allergens"),
                                nutriments = nutriments
                            )
                            _product.value = product
                        } else {
                            _error.value = "Product not found"
                        }
                    }
                } else {
                    _error.value = "API request failed"
                }
            } catch (e: Exception) {
                _error.value = "An error occurred: ${e.message}"
                Log.e("ProductViewModel", "Error fetching product", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}