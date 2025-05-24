package com.example.food_label_scanner

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import com.example.food_label_scanner.gallery_functionality.ImageDataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        val seeder = IngredientSeeder(this)
        seeder.seedIngredients()
        ImageDataStoreManager.initialize(this)
    }
}