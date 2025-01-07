package com.example.food_label_scanner.bottom_bar_drawer_content_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R

@Composable
fun Settings(){

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))


    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.Start) {
            item {
                Text(
                    text = "    Food Label",
                    style = TextStyle(
                        fontFamily = instrument_serif,
                        fontSize = 25.sp
                    )
                )
            }
        }
    }
}