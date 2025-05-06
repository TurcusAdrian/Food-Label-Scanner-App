package com.example.food_label_scanner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.food_label_scanner.database.Ingredient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class DBHelper @Inject constructor(
    @ApplicationContext context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1 // Increment the version

        //table for storing individual users
        private const val TABLE_USERS = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Table for ingredients
        private const val TABLE_INGREDIENTS = "ingredients"
        private const val COLUMN_INGREDIENT_ID = "ingredient_id"
        private const val COLUMN_INGREDIENT_NAME = "name"
        private const val COLUMN_NUTRITIONAL_VALUE = "nutritional_value"
        private const val COLUMN_CATEGORY_ID = "category_id"
        private const val COLUMN_HEALTH_RATING = "health_rating"
        private const val COLUMN_DESCRIPTION = "description"

        // Table for health categories
        private const val TABLE_HEALTH_CATEGORIES = "health_categories"
        private const val COLUMN_CATEGORY_NAME = "name"
        private const val COLUMN_CATEGORY_DESCRIPTION = "description"



    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DBHelper", "onCreate called")
        try {
            // Create users table
            val createUsersTableQuery = ("CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_USERNAME TEXT, " +
                    "$COLUMN_PASSWORD TEXT)")
            db?.execSQL(createUsersTableQuery)
            Log.d("DBHelper", "Users table created")


            // Create ingredients table
            val createIngredientsTableQuery = ("CREATE TABLE $TABLE_INGREDIENTS (" +
                    "$COLUMN_INGREDIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_INGREDIENT_NAME TEXT NOT NULL, " +
                    "$COLUMN_NUTRITIONAL_VALUE TEXT, " +
                    "$COLUMN_CATEGORY_ID INTEGER, " +
                    "$COLUMN_HEALTH_RATING INTEGER, " +
                    "$COLUMN_DESCRIPTION TEXT)")
            db?.execSQL(createIngredientsTableQuery)
            Log.d("DBHelper", "Ingredients table created")


            // Create health categories table
            val createHealthCategoriesTableQuery = ("CREATE TABLE $TABLE_HEALTH_CATEGORIES (" +
                    "$COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_CATEGORY_NAME TEXT NOT NULL, " +
                    "$COLUMN_CATEGORY_DESCRIPTION TEXT)")
            db?.execSQL(createHealthCategoriesTableQuery)
            Log.d("DBHelper", "Health Categories table created")

        } catch (e: Exception) {
            Log.e("DBHelper", "Error creating tables", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DBHelper", "onUpgrade called from $oldVersion to $newVersion")
        val dropUsersTableQuery = "DROP TABLE IF EXISTS $TABLE_USERS"
        val dropIngredientsTableQuery = "DROP TABLE IF EXISTS $TABLE_INGREDIENTS"
        db?.execSQL(dropIngredientsTableQuery)
        db?.execSQL(dropUsersTableQuery)
        onCreate(db)
    }

    // User Table Methods
    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_USERS, null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun getUserData(username: String): Cursor {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        return db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
    }

    fun getUserData(userId: Int): Cursor { // New function to get user data by ID
        val db = readableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(userId.toString())
        return db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
    }

    fun getUserId(username: String): Int {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor =
            db.query(TABLE_USERS, arrayOf(COLUMN_ID), selection, selectionArgs, null, null, null)
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        } else {
            Log.d("DBHelper", "No user found with username: $username")
        }
        cursor.close()
        Log.d("DBHelper", "Retrieved userId: $userId for username: $username")
        return userId
    }


    fun insertIngredient(
        name: String,
        nutritionalValue: String,
        categoryId: Int,
        healthRating: Int,
        description: String
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INGREDIENT_NAME, name)
            put(COLUMN_NUTRITIONAL_VALUE, nutritionalValue)
            put(COLUMN_CATEGORY_ID, categoryId)
            put(COLUMN_HEALTH_RATING, healthRating)
            put(COLUMN_DESCRIPTION, description)
        }
        db.insert(TABLE_INGREDIENTS, null, values)
    }


    fun insertCategory(
        categoryId: Int,
        name: String,
        description: String
    ){
        val db = this.writableDatabase
        val values = ContentValues().apply{
            put(COLUMN_CATEGORY_ID, categoryId)
            put(COLUMN_CATEGORY_NAME,name)
            put(COLUMN_CATEGORY_DESCRIPTION,description)
        }

        db.insert(TABLE_HEALTH_CATEGORIES, null, values)
        db.close()
    }


    fun getAllIngredients(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_INGREDIENTS, null, null, null, null, null, null)
    }

    fun getIngredientByName(ingredientName: String): Map<String, Any>? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_INGREDIENTS WHERE $COLUMN_INGREDIENT_NAME = ?"
        val cursor = try {
            db.rawQuery(query, arrayOf(ingredientName))
        } catch (e: Exception) {
            Log.e("DBHelper", "Error getting ingredient by name", e)
            return null
        }

        cursor.use {
            if (it.moveToFirst()) {
                return mapOf(
                    "name" to it.getString(it.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)),
                    "nutritional_value" to it.getString(it.getColumnIndexOrThrow(COLUMN_NUTRITIONAL_VALUE)),
                    "category_id" to it.getInt(it.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    "health_rating" to it.getInt(it.getColumnIndexOrThrow(COLUMN_HEALTH_RATING)),
                    "description" to it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                    )
            } else {
                return null // Ingredient not found
            }
        }
    }

    fun getCategoryNameById(categoryId: Int): String? {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_CATEGORY_NAME FROM $TABLE_HEALTH_CATEGORIES WHERE $COLUMN_CATEGORY_ID = ?"
        val cursor = try {
            db.rawQuery(query, arrayOf(categoryId.toString()))
        } catch (e: Exception) {
            Log.e("DBHelper", "Error getting category name by ID", e)
            return null
        }

        cursor.use {
            if (it.moveToFirst()) {
                return it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME))
            } else {
                return null // Category not found
            }
        }
    }
}
