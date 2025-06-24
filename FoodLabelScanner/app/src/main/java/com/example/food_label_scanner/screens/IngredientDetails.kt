package com.example.food_label_scanner.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.database.IngredientDetailsViewModel


@Composable
fun IngredientDetails(ingredientId: Int) {
    val viewModel: IngredientDetailsViewModel = hiltViewModel()
    val ingredient by viewModel.ingredient.collectAsState()
    val categoryName by viewModel.categoryName.collectAsState()
    val isAllergic by viewModel.isAllergic.collectAsState()

    val context = LocalContext.current
    val sharedPref = remember { context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE) }
    val userId = remember { sharedPref.getInt("userId", -1) }


    LaunchedEffect(ingredientId, userId) {
        Log.d("IngredientDetails", "Loading data with userId=$userId, ingredientId=$ingredientId")
        if (userId != -1) {
            viewModel.loadInitialData(ingredientId, userId)
        } else {
            Log.w("IngredientDetails", "userId is -1, skipping allergic data load")
        }
    }


    ingredient?.let { ingredientData -> // Renamed to avoid conflict if used in LazyColumn items
        LazyColumn( // Make the entire screen a LazyColumn
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Item for Ingredient Name
            item {
                Text(
                    text = ingredientData.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Item for Nutritional Value
            item {
                Text(
                    text = "Valoare nutrițională:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = ingredientData.nutritional_value)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Item for Category
            item {
                Text(
                    text = "Categorie:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = categoryName ?: "N/A")
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Item for Health Rating
            item {
                Text(
                    text = "Rating al sănătății:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = ingredientData.health_rating.toString())
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Item for Description
            item {
                Text(
                    text = "Descriere:",
                    fontWeight = FontWeight.Bold
                )
                Text(text = ingredientData.description)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Item for the Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            Log.d("Button click", "Button worked")
                            if (userId != -1) {
                                viewModel.toggleAllergicStatus(userId, ingredientId)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAllergic) Color.Red else Color.Green
                        )
                    ) {
                        Text(
                            text = if (isAllergic) "Remove from Allergies" else "Add to Allergies",
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

        }
    } ?: run {
        // This part remains the same
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Ingredient not found",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}