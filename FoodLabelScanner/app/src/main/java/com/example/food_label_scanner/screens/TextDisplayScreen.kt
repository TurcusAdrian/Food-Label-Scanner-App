package com.example.food_label_scanner.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.DBHelper
import com.example.food_label_scanner.barcode_functionality.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TextDisplayScreen(
    detectedText: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dbHelper = remember { DBHelper(context) }

    // Process the detected text and extract ingredients
    var arrayIngredients by remember { mutableStateOf<List<String>>(emptyList()) }
    var ingredientDetails by remember { mutableStateOf<List<String>>(emptyList()) }
    var arrayIngredientsValid by remember { mutableStateOf<List<String>>(emptyList()) }

    // Allergy checking
    val allergicViewModel: AllergicIngredientsViewModel = hiltViewModel()
    var currentUserAllergicIngredients by remember { mutableStateOf<List<String>>(emptyList()) }

    val coroutineScope = rememberCoroutineScope()


    // Collect allergic ingredients in LaunchedEffect
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userIdFromPrefs = sharedPref.getInt("userId", -1)
        if (userIdFromPrefs != -1) {
            allergicViewModel.setUserId(userIdFromPrefs)
            Log.d("TextDisplayScreen", "User ID $userIdFromPrefs set on AllergicViewModel")
            allergicViewModel.allergicIngredients.collect { ingredients ->
                currentUserAllergicIngredients = ingredients
                Log.d("TextDisplayScreen", "Allergic ingredients updated: $ingredients")
            }
        } else {
            Log.w("TextDisplayScreen", "No valid user ID found in SharedPreferences for AllergicViewModel.")
        }
    }

    // Process ingredients and fetch details
    LaunchedEffect(detectedText) {
        coroutineScope.launch(Dispatchers.IO) {
            val processedText = detectedText?.let { extractIngredientsSection(it) } ?: ""
            Log.d("DisplayImagePreview", "Processed Text: '$processedText'")
            val processed = if (processedText.isNotBlank()) process_ingredients(processedText) else ""
            Log.d("DisplayImagePreview", "After process_ingredients: '$processed'")
            val split = if (processed.isNotBlank()) splitIngredientsAdvanced(processed) else emptyList()
            Log.d("DisplayImagePreview", "After splitIngredientsAdvanced: $split")

            val validIngredients = if (split.isNotEmpty()) {
                extractValidIngredients(split, dbHelper)
            } else {
                emptyList()
            }
            val details = if (validIngredients.isNotEmpty()) {
                getIngredientDetails(validIngredients, dbHelper)
            } else {
                emptyList()
            }

            // Update state on the main thread
            withContext(Dispatchers.Main) {
                arrayIngredients = if (processed.isNotBlank()) splitIngredients(processed) else emptyList()
                arrayIngredientsValid = validIngredients
                ingredientDetails = details
                Log.d("DisplayImagePreview", "IngredientDetails: $ingredientDetails")
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        item {
            Text(
                text = "Ingredients from label scanning: $arrayIngredients",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // If no valid ingredients found, show fallback message
        if (arrayIngredientsValid.isEmpty()) {
            item {
                Text(
                    text = "No ingredients found. Try capturing another label.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        // Display ingredient details as cards
        items(
            items = ingredientDetails,
            key = { detail -> detail }
        ) { detail ->
            IngredientCard(
                detail = detail,
                allergicIngredients = currentUserAllergicIngredients
            )
        }

        // Back button at the bottom
        item {
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "Back to Camera")
            }
        }
    }
}