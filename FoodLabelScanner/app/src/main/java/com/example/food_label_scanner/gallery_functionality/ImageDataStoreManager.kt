package com.example.food_label_scanner.gallery_functionality

import android.content.Context


object ImageDataStoreManager {
    lateinit var imageDataStore: ImageDataStore private set

    fun initialize(context: Context){
        imageDataStore = ImageDataStore(context)
    }
}