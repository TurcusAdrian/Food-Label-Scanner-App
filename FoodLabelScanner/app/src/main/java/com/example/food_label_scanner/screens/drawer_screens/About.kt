package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R
import com.example.food_label_scanner.ui.theme.Cream


@Composable
fun About(){

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))


    Box(modifier = Modifier.fillMaxSize().background(Cream)){
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.Start) {
            item {
                Text(
                    text = "      Food Label Scanner este o aplicație care îi permite utilizatorului " +
                            "să știe cât de sănătos este produsul său" +
                            " în funcție de ingredientele constituente.\n\n" +
                            "     Aceasta are mai multe funcționalități precum:" +
                            " Scanarea codului de bare și Scanarea Etichetei pentru a afla date despre produs," +
                            " Galeria produselor favorite (asemeni celei din telefon)" +
                            ", funcție de captare imagine, " +
                            "căutare a ingredientelor după nume și chiar un" +
                            " dicționar de termeni nutritivi.\n\n" +
                            "     Pentru diverse probleme utilizatorul poate trimite o cerere" +
                            " la adresa de mail: foodlablscan@gmail.com\n\n" +
                            "Versiunea 1.0.0\n",
                    style = TextStyle(
                        fontFamily = instrument_serif,
                        fontSize = 25.sp
                    ),
                    color = Color.Black
                )
            }
        }
    }
}