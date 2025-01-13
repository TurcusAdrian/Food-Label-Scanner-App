package com.example.food_label_scanner

import android.content.Context
import android.util.Log

class IngredientSeeder(private val context: Context) {

    fun seedIngredients() {
        val dbHelper = DBHelper(context)

        try {

            dbHelper.insertCategory(
                categoryId = 1,
                name = "Ingredients recommended on a weekly/daily basis",
                description = "Healthy ingredients that should be consumed as often as possible for a better life"
            )

            dbHelper.insertCategory(
                categoryId = 2,
                name = "Ingredients that can be consumed once in a while",
                description = "Ingredients that aren't healthy but not that dangerous"
            )

            dbHelper.insertCategory(
                categoryId = 3,
                name = "Ingredients that should be avoided",
                description = "Ingredients that should be avoided because they provoke diseases"
            )



            // Example ingredients to insert
            dbHelper.insertIngredient(
                name = "ZAHAR",
                nutritionalValue = "High in calories",
                category = "Sweetener",
                categoryId = 2,
                healthRating = 2,
                description = "Common sugar used in foods"
            )

            dbHelper.insertIngredient(
                name = "ULEI DE PALMIER",
                nutritionalValue = "High in fats",
                category = "Oil",
                categoryId = 3,
                healthRating = 3,
                description = "Palm oil used for cooking"
            )

            dbHelper.insertIngredient(
                name = "ULEI DE SHEA",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 2,
                healthRating = 4,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertIngredient(
                name = "CARAMEL 11.8%",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 3,
                healthRating = 3,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertIngredient(
                name = "SIROP DE GLUCOZA",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 3,
                healthRating = 3,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertIngredient(
                name = "SIROP DE GLUCOZA-FRUCTOZA",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 3,
                healthRating = 2,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertIngredient(
                name = "LAPTE PRAF INTEGRAL",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 1,
                healthRating = 7,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertIngredient(
                name = "GRASIME DIN LAPTE",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 1,
                healthRating = 8,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertIngredient(
                name = "ZER PUDRA",
                nutritionalValue = "Contains healthy fats",
                category = "Oil",
                categoryId = 1,
                healthRating = 9,
                description = "Shea oil used for creams and food"
            )

            dbHelper.insertProductIngredients(
                product_id = 1,
                ingredient_ids = listOf(1, 2, 3,4,5,6,7,8,9)
            )

            dbHelper.insertUserScannedProducts(
                userId = 1,
                product_id = 1
            )

            Log.d("IngredientSeeder", "Ingredients inserted successfully")
        } catch (e: Exception) {
            Log.e("IngredientSeeder", "Error inserting ingredients", e)
        }
    }
}