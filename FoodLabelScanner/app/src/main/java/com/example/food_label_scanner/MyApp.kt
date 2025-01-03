package com.example.food_label_scanner

import dagger.hilt.android.HiltAndroidApp
import android.app.Application

@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}