package com.example.food_label_scanner.barcode_functionality

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
import android.widget.RatingBar
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.ceil

data class ParsedIngredient(
    val name: String,
    val nutritionalValue: String,
    val category: String,
    val healthRating: Int,
    val description: String
)

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

    LaunchedEffect(barcode) {
        fetchProduct(barcode) { name, ing, brnd ->
            productName = name ?: "Product not found"
            ingredients = ing ?: "Ingredients not found"
            brand = brnd ?: "Brand not found"
            val processed = process_ingredients(ingredients)
            val split = splitIngredientsAdvanced(processed)
            arrayIngredients = splitIngredients(processed)
            arrayIngredients2 = extractValidIngredients(split, dbHelper)
            ingredientDetails = getIngredientDetails(arrayIngredients2, dbHelper)
        }
    }

    LazyColumn(
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            // Display the product name
            Text(
                text = "Product: $productName",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        item {
            // Display the product name
            Text(
                text = "Array ingredients: $arrayIngredients",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        item {
            // Display the brand
            Text(
                text = "Brand: $brand",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        items(ingredientDetails) { detail ->
            IngredientCard(detail = detail)
        }
    }
}

private fun fetchProduct(
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

val ignoredKeywords = setOf(
    "EMULSIFIANTI", "CONSERVANTI", "AGENTI DE CRESTERE", "COLORANTI",
    "REGULATOR DE ACIDITATE", "STABILIZATORI", "EMULGATOR:", "AROME", "INGREDIENT:", "EMULSIFIANT:","VOPSEA:"
)

fun uppercase_text(ingredients: String): String {
    return ingredients.uppercase()
}

fun remove_diacritics(ingredients: String): String {
    val correctedIngredients = StringBuilder()

    for (char in ingredients) {
        when (char) {
            'Ă' -> correctedIngredients.append('A')
            'ă' -> correctedIngredients.append('a')
            'Â' -> correctedIngredients.append('A')
            'â' -> correctedIngredients.append('a')
            'Î' -> correctedIngredients.append('I')
            'î' -> correctedIngredients.append('i')
            'Ș' -> correctedIngredients.append('S')
            'ș' -> correctedIngredients.append('s')
            'Ț' -> correctedIngredients.append('T')
            'ț' -> correctedIngredients.append('t')
            '_' -> correctedIngredients.append("")
            '-' -> correctedIngredients.append(" ")
            else -> correctedIngredients.append(char)
        }
    }

    return correctedIngredients.toString()
}

fun extractParenthesisContent(text: String): String {
    val start = text.indexOf("(")
    val end = text.indexOf(")")
    return if (start != -1 && end != -1 && end > start) {
        text.substring(start + 1, end)
    } else {
        ""
    }
}

fun splitIngredients(processedIngredients: String): List<String> {
    // 1. Split the string by the comma delimiter
    val ingredientsList = processedIngredients.split(",")

    // 2. Trim whitespace from each ingredient (optional but recommended)
    val trimmedIngredientsList = ingredientsList.map { it.trim() }

    return trimmedIngredientsList
}

fun process_ingredients(ingredients: String): String {
    val withoutDiacritics = remove_diacritics(ingredients)
    return uppercase_text(withoutDiacritics)
}

fun splitIngredientsAdvanced(text: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var parenthesesLevel = 0

    for (char in text) {
        when (char) {
            '(' -> {
                parenthesesLevel++
                current.append(char)
            }
            ')' -> {
                parenthesesLevel--
                current.append(char)
            }
            ',' -> {
                if (parenthesesLevel == 0) {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                } else {
                    current.append(char)
                }
            }
            else -> current.append(char)
        }
    }
    if (current.isNotBlank()) {
        result.add(current.toString().trim())
    }
    return result
}

fun extractValidIngredients(rawIngredients: List<String>, dbHelper: DBHelper): List<String> {
    val validIngredients = mutableListOf<String>()

    for (ingredient in rawIngredients) {
        // Uppercase and clean
        val cleaned = ingredient.uppercase()
            .replace(Regex("^(CONSERVANT(I)?|EMULGATOR(I)?|EMULSIFIANT(I)?|COLORANT(I)?|REGULATOR DE ACIDITATE|AGENTI DE CRESTERE|VOPSEA):?\\s*"), "")

        val keyword = cleaned.split("(", ",", ";",":")[0].trim()

        if (keyword in ignoredKeywords) {
            // Check content in parentheses
            val inside = extractParenthesisContent(cleaned)
            if (inside.isNotEmpty()) {
                val nested = splitIngredientsAdvanced(inside)
                for (nestedIng in nested) {
                    val name = nestedIng.trim()
                    if (dbHelper.getIngredientByName(name) != null) {
                        validIngredients.add(name)
                    }
                }
            }
            continue
        }

        // Ingredient without parentheses
        val plain = cleaned.substringBefore("(").trim()
        if (dbHelper.getIngredientByName(plain) != null) {
            validIngredients.add(plain)
        }

        // Also consider the parenthesis content (e.g., UNT (LAIT))
        val inner = extractParenthesisContent(cleaned)
        if (inner.isNotEmpty()) {
            val innerTrimmed = inner.trim()
            if (dbHelper.getIngredientByName(innerTrimmed) != null) {
                validIngredients.add(innerTrimmed)
            }
        }
    }

    return validIngredients.distinct()
}


fun getIngredientDetails(ingredients: List<String>, dbHelper: DBHelper): List<String> {
    val details = mutableListOf<String>()
    for (ingredientName in ingredients) {
        val ingredientData = dbHelper.getIngredientByName(ingredientName)
        if (ingredientData != null) {
            val name = ingredientData["name"]
            val nutritionalValue = ingredientData["nutritional_value"]
            val categoryId = ingredientData["category_id"] as Int // Get category ID
            val healthRating = ingredientData["health_rating"]
            val description = ingredientData["description"]

            val categoryName = dbHelper.getCategoryNameById(categoryId) ?: "Category not found" // Get category name

            val detail = "Name: $name, Nutritional Value: $nutritionalValue, Category: $categoryName, Health Rating: $healthRating, Description: $description"
            details.add(detail)
        } else {
            details.add("Ingredient '$ingredientName' not found in database")
        }
    }
    return details
}

@Composable
fun IngredientCard(detail: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Parse the detail string and extract the values
            val (name, nutritionalValue, category, healthRating, description) = parseIngredientDetail(detail)

            Text(name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(category, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Health Rating:")
                RatingBar(score = healthRating)
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

    return ParsedIngredient(name, nutritionalValue, category, healthRating, description)
}

@Composable
fun RatingBar(score: Int, maxScore: Int = 5) {
    Row {
        for (i in 1..maxScore) {
            Icon(
                imageVector = when {
                    i <= score -> Icons.Filled.Star
                    i == ceil(score.toDouble()).toInt() && score % 1 != 0 -> Icons.Outlined.Star
                    else -> Icons.Outlined.Star
                },
                contentDescription = null,
                tint = Color.Yellow
            )
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
    // Convert the language code to a TranslateLanguage constant
    val sourceLanguage = when (languageCode) {
        "en" -> TranslateLanguage.ENGLISH
        "fr" -> TranslateLanguage.FRENCH
        "de" -> TranslateLanguage.GERMAN
        "pl" -> TranslateLanguage.POLISH
        // Add more cases as needed
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
