package com.example.food_label_scanner.barcode_functionality

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.DBHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

import android.database.Cursor
import android.net.ConnectivityManager
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.food_label_scanner.NotificationHelper
import com.example.food_label_scanner.R
import com.example.food_label_scanner.screens.AllergicIngredientsViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ceil


data class ParsedIngredient(
    val name: String,
    val nutritionalValue: String,
    val category: String,
    val healthRating: Int,
    val description: String
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "FlowOperatorInvokedInComposition")
@Composable
fun BarcodeDisplayScreen(barcode: String) {
    var productName by remember { mutableStateOf("Loading...") }
    var ingredients by remember { mutableStateOf("Loading...") }
    var brand by remember { mutableStateOf("Loading...") }

    val context = LocalContext.current

    val dbHelper = remember { DBHelper(context) }
    var arrayIngredients: List<String> by remember { mutableStateOf(emptyList()) }
    var ingredientDetails: List<String> by remember { mutableStateOf(emptyList()) }

    var arrayIngredients2: List<String> by remember { mutableStateOf(emptyList()) }


    val allergicViewModel: AllergicIngredientsViewModel = hiltViewModel()
    var currentUserAllergicIngredients by remember { mutableStateOf<List<String>>(emptyList()) }

    // Collect allergic ingredients in LaunchedEffect
    LaunchedEffect(Unit) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userIdFromPrefs = sharedPref.getInt("userId", -1)
        if (userIdFromPrefs != -1) {
            allergicViewModel.setUserId(userIdFromPrefs)
            Log.d("BarcodeScreen", "User ID $userIdFromPrefs set on AllergicViewModel")
            allergicViewModel.allergicIngredients.collect { ingredients ->
                currentUserAllergicIngredients = ingredients
                Log.d("BarcodeScreen", "Allergic ingredients updated: $ingredients")
            }
        } else {
            Log.w("BarcodeScreen", "No valid user ID found in SharedPreferences for AllergicViewModel.")
        }
    }



    LaunchedEffect(barcode) {
        fetchProduct(barcode) { name, ing, brnd ->
            productName = name ?: "Product not found"
            ingredients = ing ?: "Ingredients not found"
            brand = brnd ?: "Brand not found"
            val processed = process_ingredients(ingredients)
            val split = splitIngredientsAdvanced(processed)
            arrayIngredients = splitIngredients(processed) //used normal split to show them as similar to the label as possible
            arrayIngredients2 = extractValidIngredients(split, dbHelper) //used advanced split to find ingredients from (), good when printing results
            ingredientDetails = getIngredientDetails(arrayIngredients2, dbHelper)
            Log.d("BarcodeScreen", "IngredientDetails: $ingredientDetails")
        }
    }


    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = "Product: $productName",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        item {
            Text(
                text = "Ingredients: $arrayIngredients",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        item {
            Text(
                text = "Brand: $brand",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // If no valid ingredients found, show fallback message
        if (arrayIngredients2.isEmpty()) {
            item {
                Text(
                    text = "No ingredients found. Try using Label Scanning instead.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }

        items(
            items = ingredientDetails,
            key = { detail -> detail
                // Ensure unique key, fallback to index if parsing fails
                //val name = detail.substringAfter("Name: ").substringBefore(",").trim()
                //name.ifEmpty { "ingredient_${ingredientDetails.indexOf(detail)}" }
            }
        ) { detail ->
            IngredientCard(
                detail = detail,
                allergicIngredients = currentUserAllergicIngredients
            )
        }
    }
}


fun fetchProduct(
    barcode: String,
    onResult: (String?, String?, String?) -> Unit
) {
    val client = OkHttpClient()
    val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json"
    val request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            onResult(null, null, null)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            val responseBody = response.body?.string()
            if (response.isSuccessful && responseBody != null) {
                try {
                    val json = JSONObject(responseBody)
                    if (json.has("product")) {
                        val product = json.getJSONObject("product")
                        val name = product.optString("product_name", null)
                        var ing = product.optString("ingredients_text", null)
                        val brnd = product.optString("brands", null)

                        if (ing != null) {
                            identifyLanguage(ing!!) { languageCode ->
                                if (languageCode != null && languageCode != "ro") {
                                    translateToRomanian(ing!!,languageCode) { translatedText ->
                                        ing = translatedText
                                        CoroutineScope(Dispatchers.Main).launch {
                                            onResult(name, ing, brnd)
                                        }
                                    }
                                } else {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        onResult(name, ing, brnd)
                                    }
                                }
                            }
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
                                onResult(name, ing, brnd)
                            }
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            onResult("Product not found", null, null)
                        }
                    }
                } catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        onResult(null, null, null)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    onResult(null, null, null)
                }
            }
        }
    })
}



@Composable
fun IngredientCard(detail: String, allergicIngredients: List<String>) {
    val (name, nutritionalValue, category, healthRating, description) = parseIngredientDetail(detail)
    val isAllergicToThisIngredient = name.isNotEmpty() && allergicIngredients.contains(name)

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Name and Category
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(category, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Description
            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Health Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Health Rating:   ")
                RatingIndicator(healthRating) //score = healthRating
            }

            // Allergic Warning (if applicable)
            if (isAllergicToThisIngredient) {
                Log.d("IngredientCard", "Rendering allergic text for: $name, Allergic list: $allergicIngredients")
                Text(
                    text = "You've marked this as an allergen.",
                    color = Color.Red,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Log.d("IngredientCard", "Not rendering allergic text for: $name, Allergic list: $allergicIngredients")
            }
        }
    }
}

fun parseIngredientDetail(detail: String): ParsedIngredient {
    val nameRegex = Regex("Name: (.*?),")
    val nutritionalValueRegex = Regex("Nutritional Value: (.*?),")
    val categoryRegex = Regex("Category: (.*?),")
    val healthRatingRegex = Regex("Health Rating: (.*?),")
    val descriptionRegex = Regex("Description: (.*)")

    val nameMatch = nameRegex.find(detail)
    val nutritionalValueMatch = nutritionalValueRegex.find(detail)
    val categoryMatch = categoryRegex.find(detail)
    val healthRatingMatch = healthRatingRegex.find(detail)
    val descriptionMatch = descriptionRegex.find(detail)

    val name = nameMatch?.groupValues?.get(1) ?: "Unknown"
    val nutritionalValue = nutritionalValueMatch?.groupValues?.get(1) ?: "Unknown"
    val category = categoryMatch?.groupValues?.get(1) ?: "Unknown"
    val healthRating = healthRatingMatch?.groupValues?.get(1)?.toIntOrNull() ?: 0
    val description = descriptionMatch?.groupValues?.get(1) ?: "Unknown"

    Log.d("ParseIngredientDetail", "Detail: '$detail', Parsed Health Rating: $healthRating")

    return ParsedIngredient(name, nutritionalValue, category, healthRating, description)
}



@Composable
fun RatingIndicator(healthRating: Int) {
    when (healthRating) {
        in 4..5 -> {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Good",
                tint = Color.Green,
                modifier = Modifier.size(32.dp)
            )
        }
        3 -> {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Average",
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(32.dp)
            )
        }
        in 1..2 -> {
            Icon(
                painter = painterResource(id = R.drawable.thumbs_down_red),
                contentDescription = "Bad",
                tint = Color(0xFFFF0000),
                modifier = Modifier.size(32.dp)
            )
        }
        else -> {
            Text("Incorrect rating - please ask developer for details")
        }
    }
}

fun identifyLanguage(text: String, onResult: (String?) -> Unit) {
    val languageIdentifier = com.google.mlkit.nl.languageid.LanguageIdentification.getClient()
    languageIdentifier.identifyLanguage(text)
        .addOnSuccessListener { languageCode ->
            if (languageCode == "und") {
                Log.i("Language", "Can't identify language.")
                onResult(null)
            } else {
                Log.i("Language", "Language: $languageCode")
                onResult(languageCode)
            }
        }
        .addOnFailureListener {
            Log.e("Language", "Error identifying language: ${it.message}")
            onResult(null)
        }
}


fun translateToRomanian(text: String, languageCode: String, onResult: (String?) -> Unit) {

    val sourceLanguage = when (languageCode) {
        "en" -> TranslateLanguage.ENGLISH
        "fr" -> TranslateLanguage.FRENCH
        "de" -> TranslateLanguage.GERMAN
        "pl" -> TranslateLanguage.POLISH
        else -> {
            Log.e("Translation", "Unsupported source language: $languageCode")
            onResult(null)
            return // Exit the function if the language is not supported
        }
    }

    val options = TranslatorOptions.Builder()
        .setSourceLanguage(sourceLanguage)
        .setTargetLanguage(TranslateLanguage.ROMANIAN)
        .build()
    val translator = Translation.getClient(options)

    // Download the translation model if needed
    val conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()
    translator.downloadModelIfNeeded(conditions)
        .addOnSuccessListener {
            Log.i("Translation", "Translation model downloaded successfully.")
            translator.translate(text)
                .addOnSuccessListener { translatedText ->
                    Log.i("Translation", "Translated text: $translatedText")
                    onResult(translatedText)
                }
                .addOnFailureListener {
                    Log.e("Translation", "Error translating text: ${it.message}")
                    onResult(null)
                }
        }
        .addOnFailureListener {
            Log.e("Translation", "Error downloading translation model: ${it.message}")
            onResult(null)
        }
}


