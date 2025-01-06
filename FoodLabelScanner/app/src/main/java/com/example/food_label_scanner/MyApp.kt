package com.example.food_label_scanner

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import com.example.food_label_scanner.data.ImageDataStoreManager

@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        ImageDataStoreManager.initialize(this)
    }
}