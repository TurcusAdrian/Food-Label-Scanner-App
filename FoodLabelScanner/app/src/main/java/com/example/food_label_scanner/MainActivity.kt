package com.example.food_label_scanner

import com.example.food_label_scanner.ui_elements.*
import com.example.food_label_scanner.camera_functionality.*

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.example.food_label_scanner.ui.theme.FoodLabelScannerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodLabelScannerTheme {
                Screen()
            }
        }
    }
}











