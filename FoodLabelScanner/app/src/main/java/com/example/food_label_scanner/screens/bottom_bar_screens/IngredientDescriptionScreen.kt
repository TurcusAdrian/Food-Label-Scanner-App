package com.example.food_label_scanner.screens.bottom_bar_screens

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
import androidx.navigation.compose.rememberNavController
import com.example.food_label_scanner.R
import com.example.food_label_scanner.screens.Screens


@Composable
fun IngredientDescriptionScreen(){

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))



    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            horizontalAlignment = Alignment.Start) {
            item {
                Text(
                    text = "    Food Label Scanner app is an application that allows the user to know how healthy his/her food is depending on the ingredients that it contains.\n\n" +
                            "   It works in a simple manner: You either take a photo with the built-in 'Take Picture' button and choose it from the gallery with the 'Add Photos from Gallery'" +
                            " or you could just simply put the desired product in front of the camera, it will then process what it sees and then tell you how good the food is or not.\n\n" +
                            "   The app has other functions like 'Friends' where you could share different scanned products, 'Search' which let's you search for a different ingredient in our database" +
                            "or Favourites Items where you can save your desired products so you can have a collection of healthy food right in your account.\n\n" +
                            "   Future Improvements include Barcode Scanning and fruit/vegetables rating through zone analysis\n\n" +
                            "Version : 1.0.0\n",
                    style = TextStyle(
                        fontFamily = instrument_serif,
                        fontSize = 25.sp
                    )
                )
            }
        }
    }
}