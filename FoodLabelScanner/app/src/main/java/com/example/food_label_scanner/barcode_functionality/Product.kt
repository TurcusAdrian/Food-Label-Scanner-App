package com.example.food_label_scanner.barcode_functionality

data class Product(
    val productName: String? = null,
    val ingredients: String? = null,
    val imageUrl: String? = null,
    val brands: String? = null,
    val categories: String? = null,
    val countries: String? = null,
    val allergens: String? = null,
    val nutriments: Nutriments? = null
)