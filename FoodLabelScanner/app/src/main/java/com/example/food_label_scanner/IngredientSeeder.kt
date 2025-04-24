package com.example.food_label_scanner

import android.content.Context
import android.util.Log

class IngredientSeeder(private val context: Context) {

    fun seedIngredients() {
        val dbHelper = DBHelper(context)
        val db = dbHelper.readableDatabase

        try {
            // Insert categories if they don’t exist
            insertCategoryIfNotExists(dbHelper, 1, "Sweeteners", "Ingredients used to add sweetness, natural or artificial")
            insertCategoryIfNotExists(dbHelper, 2, "Fats and Oils", "Sources of dietary fats, used for cooking or texture")
            insertCategoryIfNotExists(dbHelper, 3, "Preservatives", "Used to extend shelf life and prevent spoilage")
            insertCategoryIfNotExists(dbHelper, 4, "Thickeners / Stabilizers", "Improve texture or consistency, often from gums or starches")
            insertCategoryIfNotExists(dbHelper, 5, "Emulsifiers", "Allow mixing of oil and water, stabilize mixtures")
            insertCategoryIfNotExists(dbHelper, 6, "Acidifiers / Acidity Regulators", "Control acidity to affect taste or preservation")
            insertCategoryIfNotExists(dbHelper, 7, "Flavorings", "Enhance or simulate flavors, can be natural or artificial")
            insertCategoryIfNotExists(dbHelper, 8, "Colorants", "Used to alter or enhance the color of food products")
            insertCategoryIfNotExists(dbHelper, 9, "Dairy", "Milk-based ingredients used for taste, texture, or nutrition")
            insertCategoryIfNotExists(dbHelper, 10, "Grains / Flours", "Base carbohydrate sources for structure and bulk")
            insertCategoryIfNotExists(dbHelper, 11, "Proteins", "Protein-rich ingredients from animal or plant sources")
            insertCategoryIfNotExists(dbHelper, 12, "Vitamins / Minerals", "Nutrients added for health or regulatory reasons")
            insertCategoryIfNotExists(dbHelper, 13, "Additives (other)", "Functional agents like anti-caking, humectants, etc.")
            insertCategoryIfNotExists(dbHelper, 14, "Nuts & Seeds", "Plant-based sources of fat, protein, and fiber")
            insertCategoryIfNotExists(dbHelper, 15, "Fruits & Vegetables", "Plant-based ingredients used for flavor or nutrition")


            // Insert ingredients only if they don't already exist
            insertIngredientIfNotExists(dbHelper, "ZAHAR", "High in calories",  2, 1, "Common sugar used in foods")
            insertIngredientIfNotExists(dbHelper, "ULEI DE PALMIER", "High in fats",  3, 2, "Palm oil used for cooking")
            insertIngredientIfNotExists(dbHelper, "ULEI DE SHEA", "Contains healthy fats",  2, 2, "Shea oil used for creams and food")
            insertIngredientIfNotExists(dbHelper, "CARAMEL 11.8%", "High in sugar content",  3, 8, "Commonly used for coloring and flavoring")
            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA", "High glycemic index",  3, 1, "Glucose syrup used in processed foods")
            insertIngredientIfNotExists(dbHelper, "SIROP DE GLUCOZA-FRUCTOZA", "Linked to metabolic issues",  3, 1, "A mixture of glucose and fructose used in sweetened beverages")
            insertIngredientIfNotExists(dbHelper, "LAPTE PRAF INTEGRAL", "Good source of calcium",  1, 9, "Whole milk powder used in various dairy products")
            insertIngredientIfNotExists(dbHelper, "GRASIME DIN LAPTE", "Contains essential fatty acids",  1, 9, "Milk fat used in butter and dairy processing")
            insertIngredientIfNotExists(dbHelper, "ZER PUDRA", "Rich in protein", 1, 9, "Whey powder used as a protein supplement")
            insertIngredientIfNotExists(dbHelper, "ACID CITRIC ANHIDRU", "Contains antioxidants and supports metabolism",  1, 6, "A beneficial ingredient often found in citrus fruits, commonly used as a preservative and acidity regulator.")

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
        categoryId: Int,
        healthRating: Int,
        description: String
    ) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT 1 FROM ingredients WHERE name = ?", arrayOf(name))
        val exists = cursor.moveToFirst()
        cursor.close()

        if (!exists) {
            dbHelper.insertIngredient(name, nutritionalValue, categoryId, healthRating, description)
        }
    }
}