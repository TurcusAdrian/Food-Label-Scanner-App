package com.example.food_label_scanner.data

data class Section(
    val title: String,
    val content: String,
    var isExpanded: Boolean = false,
    val imageResId: Int? = null // Optional image resource ID

)