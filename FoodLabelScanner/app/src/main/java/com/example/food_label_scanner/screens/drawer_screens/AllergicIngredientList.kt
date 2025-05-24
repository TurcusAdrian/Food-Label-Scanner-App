package com.example.food_label_scanner.screens

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.copy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.database.IngredientDetailsViewModel

@Composable
fun AllergicIngredientsList() {
    val viewModel: AllergicIngredientsViewModel = hiltViewModel()
    val context = LocalContext.current // For getting userId initially

    // Get userId and set it in the ViewModel
    // This should ideally happen once or when userId changes.
    LaunchedEffect(Unit) { // Using Unit to run once on composition
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        // Ensure this key "user_id" matches exactly what you use when saving it.
        // Common variations include "userId", "USER_ID", etc.
        val userId = sharedPref.getInt("userId", -1)
        if (userId != -1) {
            Log.d("AllergicList", "Setting userId in ViewModel: $userId")
            viewModel.setUserId(userId)
        } else {
            Log.w("AllergicList", "No valid userId found in SharedPreferences.")
            // Optionally, you could show a message to the user or handle this state.
            // For now, the ViewModel's flow will default to an empty list.
        }
    }

    val allergicIngredients by viewModel.allergicIngredients.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize() // This will make the Column try to take all available space.
            // If this Composable is part of a larger screen with other elements,
            // you might want to use Modifier.fillMaxWidth() and potentially a
            // Modifier.height() or Modifier.weight(1f) if it's inside another Column/Row.
            .padding(16.dp)
    ) {
        Text(
            text = "Your Allergic Ingredients",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleLarge // Using MaterialTheme for consistency
        )
        Spacer(modifier = Modifier.height(16.dp)) // Increased spacer a bit

        if (allergicIngredients.isEmpty()) {
            Box( // Use a Box to center the text if the list is empty
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp), // Add some vertical padding
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You haven't marked any ingredients as allergenic yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize() // Allow LazyColumn to take available space within this Column
            ) {
                items(
                    items = allergicIngredients,
                    key = { ingredientName -> ingredientName } // Provide a stable key if names are unique
                ) { ingredientName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• $ingredientName",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f) // Allow text to take available width
                        )
                        // Optionally, you could add a small remove icon/button here
                        // if you want to allow removal directly from this list.
                        // For now, removal is handled in IngredientDetails.
                    }
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) // Use theme color for divider
                }
            }
        }
    }
}