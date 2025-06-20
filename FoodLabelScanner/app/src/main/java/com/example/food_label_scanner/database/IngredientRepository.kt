package com.example.food_label_scanner.database

import android.database.Cursor
import com.example.food_label_scanner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class IngredientRepository @Inject constructor(private val dbHelper: DBHelper) {

    suspend fun getAllIngredients(): List<Ingredient> = withContext(Dispatchers.IO) {
        val ingredients = mutableListOf<Ingredient>()
        val cursor: Cursor = dbHelper.getAllIngredients()
        try {
            if (cursor.moveToFirst()) {
                do {
                    val ingredient = Ingredient(
                        ingredient_id = cursor.getInt(cursor.getColumnIndexOrThrow("ingredient_id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        nutritional_value = cursor.getString(cursor.getColumnIndexOrThrow("nutritional_value")),
                        category_id = cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        health_rating = cursor.getInt(cursor.getColumnIndexOrThrow("health_rating")),
                        description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                    )
                    ingredients.add(ingredient)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor.close()
        }
        ingredients
    }
}