package com.example.food_label_scanner.ui_elements

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import coil.compose.AsyncImage
import com.example.food_label_scanner.DBHelper
import com.example.food_label_scanner.barcode_functionality.*

import com.example.food_label_scanner.screens.AllergicIngredientsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DisplayImagePreview(
    modifier: Modifier = Modifier,
    selectedImage: Uri?,
    detectedText: String?,
    onImageClick: () -> Unit
) {
    if (selectedImage != null && !detectedText.isNullOrBlank()) {
        val context = LocalContext.current
        val dbHelper = remember { DBHelper(context) }
        val coroutineScope = rememberCoroutineScope()

        // Process the detected text and extract ingredients
        var arrayIngredients by remember { mutableStateOf<List<String>>(emptyList()) }
        var ingredientDetails by remember { mutableStateOf<List<String>>(emptyList()) }
        var arrayIngredientsValid by remember { mutableStateOf<List<String>>(emptyList()) }

        // Allergy checking
        val allergicViewModel: AllergicIngredientsViewModel = hiltViewModel()
        var currentUserAllergicIngredients by remember { mutableStateOf<List<String>>(emptyList()) }

        // Collect allergic ingredients
        LaunchedEffect(Unit) {
            val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            val userIdFromPrefs = sharedPref.getInt("userId", -1)
            if (userIdFromPrefs != -1) {
                allergicViewModel.setUserId(userIdFromPrefs)
                Log.d("DisplayImagePreview", "User ID $userIdFromPrefs set on AllergicViewModel")
                allergicViewModel.allergicIngredients.collect { ingredients ->
                    currentUserAllergicIngredients = ingredients
                    Log.d("DisplayImagePreview", "Allergic ingredients updated: $ingredients")
                }
            } else {
                Log.w("DisplayImagePreview", "No valid user ID found in SharedPreferences for AllergicViewModel.")
            }
        }

        // Process ingredients and fetch details in a background thread
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
            // Display the smaller selected image at the top
            item {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .clickable { onImageClick() }
                        .size(width = 200.dp, height = 200.dp),
                        //.align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Array of ingredients (joined for display as a single string)
            item {
                Text(
                    text = "Ingredients from scanning the photo: ${arrayIngredients.joinToString(", ")}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Fallback message if no valid ingredients
            if (arrayIngredientsValid.isEmpty()) {
                item {
                    Text(
                        text = "No valid ingredients found. Try another image.",
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
                Card(
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp, horizontal = 4.dp)
                ) {
                    IngredientCard(
                        detail = detail,
                        allergicIngredients = currentUserAllergicIngredients,
                    )
                }
            }

            // Back button at the bottom
            item {
                Button(
                    onClick = onImageClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
                ) {
                    Text(text = "Back to Gallery", color = Color.White)
                }
            }
        }
    } else {
        // Fallback if no image or text is available
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No image or text detected. Please select an image.")
        }
    }
}