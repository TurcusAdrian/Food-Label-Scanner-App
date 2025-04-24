package com.example.food_label_scanner.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.database.IngredientDetailsViewModel

@Composable
fun IngredientDetails(ingredientId: Int) {
    val viewModel: IngredientDetailsViewModel = hiltViewModel()
    val ingredient by viewModel.ingredient.collectAsState()

    LaunchedEffect(key1 = ingredientId) {
        viewModel.loadIngredient(ingredientId)
    }

    ingredient?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = it.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nutritional Value:",
                fontWeight = FontWeight.Bold
            )
            Text(text = it.nutritional_value)
            Spacer(modifier = Modifier.height(8.dp))
            //add category to show through category_id
            Text(
                text = "Health Rating:",
                fontWeight = FontWeight.Bold
            )
            Text(text = it.health_rating.toString())
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Description:",
                fontWeight = FontWeight.Bold
            )
            Text(text = it.description)
        }
    }
}