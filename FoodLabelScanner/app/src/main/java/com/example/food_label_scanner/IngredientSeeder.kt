package com.example.food_label_scanner

import android.content.Context
import android.util.Log

class IngredientSeeder(private val context: Context) {

    fun seedIngredients() {
        val dbHelper = DBHelper(context)
        val db = dbHelper.readableDatabase

        try {
            // Insert categories if they don’t exist
            insertCategoryIfNotExists(dbHelper, 1, "Ingredients recommended on a weekly/daily basis", "Healthy ingredients that should be consumed as often as possible for a better life")
            insertCategoryIfNotExists(dbHelper, 2, "Ingredients that can be consumed once in a while", "Ingredients that aren't healthy but not that dangerous")
            insertCategoryIfNotExists(dbHelper, 3, "Ingredients that should be avoided", "Ingredients that should be avoided because they provoke diseases")

            // Insert ingredients only if they don't already exist
            insertIngredientIfNotExists(dbHelper, "ZAHAR", "High in calories", "Sweetener", 2, 2, "Common sugar used in foods")
            insertIngredientIfNotExists(dbHelper, "ULEI DE PALMIER", "High in fats", "Oil", 3, 3, "Palm oil used for cooking")
            insertIngredientIfNotExists(dbHelper, "ULEI DE SHEA", "Contains healthy fats", "Oil", 2, 4, "Shea oil used for creams and food")
            insertIngredientIfNotExists(dbHelper, "CARAMEL 11.8%", "High in sugar content", "Sweetener", 3, 3, "Commonly used for coloring and flavoring")
            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA", "High glycemic index", "Sweetener", 3, 3, "Glucose syrup used in processed foods")
            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA-FRUCTOZA", "Linked to metabolic issues", "Sweetener", 3, 2, "A mixture of glucose and fructose used in sweetened beverages")
            insertIngredientIfNotExists(dbHelper, "LAPTE PRAF INTEGRAL", "Good source of calcium", "Dairy", 1, 7, "Whole milk powder used in various dairy products")
            insertIngredientIfNotExists(dbHelper, "GRASIME DIN LAPTE", "Contains essential fatty acids", "Dairy", 1, 8, "Milk fat used in butter and dairy processing")
            insertIngredientIfNotExists(dbHelper, "ZER PUDRA", "Rich in protein", "Dairy", 1, 9, "Whey powder used as a protein supplement")
            insertIngredientIfNotExists(dbHelper, "ACID CITRIC ANHIDRU", "Contains antioxidants and supports metabolism", "Preservative", 1, 9, "A beneficial ingredient often found in citrus fruits, commonly used as a preservative and acidity regulator.")

            Log.d("IngredientSeeder", "Seeding complete")
        } catch (e: Exception) {
            Log.e("IngredientSeeder", "Error seeding ingredients", e)
        } finally {
            db.close()
        }
    }

    private fun insertCategoryIfNotExists(dbHelper: DBHelper, categoryId: Int, name: String, description: String) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM health_categories WHERE category_id = ?", arrayOf(categoryId.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()

        if (!exists) {
            dbHelper.insertCategory(categoryId, name, description)
        }
    }

    private fun insertIngredientIfNotExists(
        dbHelper: DBHelper,
        name: String,
        nutritionalValue: String,
        category: String,
        categoryId: Int,
        healthRating: Int,
        description: String
    ) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM ingredients WHERE name = ?", arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()

        if (!exists) {
            dbHelper.insertIngredient(name, nutritionalValue, category, categoryId, healthRating, description)
        }
    }
}