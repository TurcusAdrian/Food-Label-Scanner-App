package com.example.food_label_scanner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.distinctUntilChanged
import com.example.food_label_scanner.database.Ingredient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject




class DBHelper @Inject constructor(
    @ApplicationContext context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        private const val TABLE_INGREDIENTS = "ingredients"
        private const val COLUMN_INGREDIENT_ID = "ingredient_id"
        private const val COLUMN_INGREDIENT_NAME = "name"
        private const val COLUMN_NUTRITIONAL_VALUE = "nutritional_value"
        private const val COLUMN_CATEGORY_ID = "category_id"
        private const val COLUMN_HEALTH_RATING = "health_rating"
        private const val COLUMN_DESCRIPTION = "description"

        private const val TABLE_HEALTH_CATEGORIES = "health_categories"
        private const val COLUMN_CATEGORY_NAME = "name"
        private const val COLUMN_CATEGORY_DESCRIPTION = "description"

        private const val TABLE_ALLERGIC_INGREDIENTS = "allergic_ingredients"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_ID_INGREDIENT = "ingredient_id"

        @Volatile
        private var instance: DBHelper? = null

        fun getInstance(context: Context): DBHelper =
            instance ?: synchronized(this) {
                instance ?: DBHelper(context.applicationContext).also { instance = it }
            }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DBHelper", "onCreate called")
        try {
            val createUsersTableQuery = ("CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_USERNAME TEXT, " +
                    "$COLUMN_PASSWORD TEXT)")
            db?.execSQL(createUsersTableQuery)
            Log.d("DBHelper", "Users table created")

            val createHealthCategoriesTableQuery = ("CREATE TABLE $TABLE_HEALTH_CATEGORIES (" +
                    "$COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_CATEGORY_NAME TEXT NOT NULL, " +
                    "$COLUMN_CATEGORY_DESCRIPTION TEXT)")
            db?.execSQL(createHealthCategoriesTableQuery)
            Log.d("DBHelper", "Health Categories table created")

            val createIngredientsTableQuery = ("CREATE TABLE $TABLE_INGREDIENTS (" +
                    "$COLUMN_INGREDIENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_INGREDIENT_NAME TEXT NOT NULL, " +
                    "$COLUMN_NUTRITIONAL_VALUE TEXT, " +
                    "$COLUMN_CATEGORY_ID INTEGER, " +
                    "$COLUMN_HEALTH_RATING INTEGER, " +
                    "$COLUMN_DESCRIPTION TEXT, " +
                    "FOREIGN KEY ($COLUMN_CATEGORY_ID) REFERENCES $TABLE_HEALTH_CATEGORIES($COLUMN_CATEGORY_ID))")
            db?.execSQL(createIngredientsTableQuery)
            Log.d("DBHelper", "Ingredients table created")

            val createAllergicTable = """
            CREATE TABLE $TABLE_ALLERGIC_INGREDIENTS (
                $COLUMN_USER_ID INTEGER,
                $COLUMN_ID_INGREDIENT INTEGER,
                PRIMARY KEY ($COLUMN_USER_ID, $COLUMN_ID_INGREDIENT),
                FOREIGN KEY ($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY ($COLUMN_ID_INGREDIENT) REFERENCES $TABLE_INGREDIENTS($COLUMN_INGREDIENT_ID) ON DELETE CASCADE ON UPDATE CASCADE
            )
            """.trimIndent()
            db?.execSQL(createAllergicTable)
            Log.d("DBHelper", "Allergic table created")
        } catch (e: Exception) {
            Log.e("DBHelper", "Error creating tables", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DBHelper", "onUpgrade called from $oldVersion to $newVersion")
        val dropUsersTableQuery = "DROP TABLE IF EXISTS $TABLE_USERS"
        val dropIngredientsTableQuery = "DROP TABLE IF EXISTS $TABLE_INGREDIENTS"
        val dropCategoriesTableQuery = "DROP TABLE IF EXISTS $TABLE_HEALTH_CATEGORIES"
        val dropAllergicTableQuery = "DROP TABLE IF EXISTS $TABLE_ALLERGIC_INGREDIENTS"

        db?.execSQL(dropAllergicTableQuery)
        db?.execSQL(dropIngredientsTableQuery)
        db?.execSQL(dropCategoriesTableQuery)
        db?.execSQL(dropUsersTableQuery)
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        try {
            db.execSQL("PRAGMA foreign_keys=ON;")
            Log.d("DBHelper", "Foreign keys enabled")
        } catch (e: SQLiteException) {
            Log.e("DBHelper", "Error in onOpen: ${e.message}", e)
            throw e
        }
    }

    // User Table Methods
    fun insertUser(username: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return try {
            db.insert(TABLE_USERS, null, values)
        } finally {
            db.close() // Close only after the operation, but let SQLiteOpenHelper manage the pool
        }
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        return try {
            cursor.count > 0
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun getUserData(username: String): Cursor {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        return db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null).also {
            // Return cursor without closing db here; caller must close cursor
        }
    }

    fun getUserData(userId: Int): Cursor {
        val db = readableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(userId.toString())
        return db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null).also {
            // Return cursor without closing db here; caller must close cursor
        }
    }

    fun getUserId(username: String): Int {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_ID), selection, selectionArgs, null, null, null)
        return try {
            if (cursor.moveToFirst()) {
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                Log.d("DBHelper", "Retrieved userId: $userId for username: $username")
                userId
            } else {
                Log.d("DBHelper", "No user found with username: $username")
                -1
            }
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun insertIngredient(
        name: String,
        nutritionalValue: String,
        categoryId: Int,
        healthRating: Int,
        description: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_INGREDIENT_NAME, name)
            put(COLUMN_NUTRITIONAL_VALUE, nutritionalValue)
            put(COLUMN_CATEGORY_ID, categoryId)
            put(COLUMN_HEALTH_RATING, healthRating)
            put(COLUMN_DESCRIPTION, description)
        }
        return try {
            db.insert(TABLE_INGREDIENTS, null, values)
        } finally {
            db.close()
        }
    }

    fun insertCategory(
        categoryId: Int,
        name: String,
        description: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_ID, categoryId)
            put(COLUMN_CATEGORY_NAME, name)
            put(COLUMN_CATEGORY_DESCRIPTION, description)
        }
        return try {
            db.insert(TABLE_HEALTH_CATEGORIES, null, values)
        } finally {
            db.close()
        }
    }

    fun getAllIngredients(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_INGREDIENTS, null, null, null, null, null, null).also {
            // Return cursor without closing db here; caller must close cursor
        }
    }

    fun getIngredientByName(ingredientName: String): Map<String, Any>? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_INGREDIENTS WHERE $COLUMN_INGREDIENT_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(ingredientName))
        return try {
            if (cursor.moveToFirst()) {
                mapOf(
                    "name" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)),
                    "nutritional_value" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NUTRITIONAL_VALUE)),
                    "category_id" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                    "health_rating" to cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEALTH_RATING)),
                    "description" to cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                )
            } else {
                null
            }
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun getCategoryNameById(categoryId: Int): String? {
        val db = readableDatabase
        val query = "SELECT $COLUMN_CATEGORY_NAME FROM $TABLE_HEALTH_CATEGORIES WHERE $COLUMN_CATEGORY_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(categoryId.toString()))
        return try {
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME))
            } else {
                null
            }
        } finally {
            cursor.close()
            db.close()
        }
    }

    private val _allergyChanges = MutableSharedFlow<Unit>(replay = 1)
    init {
        _allergyChanges.tryEmit(Unit)
    }

    private fun notifyAllergyChanged() {
        _allergyChanges.tryEmit(Unit)
    }

    private fun doesUserExistInternal(db: SQLiteDatabase, userId: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_USERS WHERE $COLUMN_ID = ?",
            arrayOf(userId.toString())
        )
        return try {
            cursor.moveToFirst()
        } finally {
            cursor.close()
        }
    }

    private fun doesIngredientExistInternal(db: SQLiteDatabase, ingredientId: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_INGREDIENTS WHERE $COLUMN_INGREDIENT_ID = ?",
            arrayOf(ingredientId.toString())
        )
        return try {
            cursor.moveToFirst()
        } finally {
            cursor.close()
        }
    }

    private fun isIngredientAllergicInternal(db: SQLiteDatabase, userId: Int, ingredientId: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_ALLERGIC_INGREDIENTS WHERE $COLUMN_USER_ID=? AND $COLUMN_ID_INGREDIENT=?",
            arrayOf(userId.toString(), ingredientId.toString())
        )
        return try {
            cursor.moveToFirst()
        } finally {
            cursor.close()
        }
    }

    fun doesIngredientExist(ingredientId: Int): Boolean {
        val db = readableDatabase
        return try {
            doesIngredientExistInternal(db, ingredientId)
        } finally {
            db.close()
        }
    }

    fun doesUserExist(userId: Int): Boolean {
        val db = readableDatabase
        return try {
            doesUserExistInternal(db, userId)
        } finally {
            db.close()
        }
    }

    fun addAllergicIngredient(userId: Int, ingredientId: Int): Boolean {
        val db = writableDatabase
        return try {
            if (!doesUserExistInternal(db, userId)) {
                Log.e("DBHelper", "Add failed: User with ID $userId does not exist.")
                false
            } else if (!doesIngredientExistInternal(db, ingredientId)) {
                Log.e("DBHelper", "Add failed: Ingredient with ID $ingredientId does not exist.")
                false
            } else if (isIngredientAllergicInternal(db, userId, ingredientId)) {
                Log.d("DBHelper", "User $userId is already allergic to ingredient $ingredientId. No change made.")
                true
            } else {
                val values = ContentValues().apply {
                    put(COLUMN_USER_ID, userId)
                    put(COLUMN_ID_INGREDIENT, ingredientId)
                }
                val result = db.insert(TABLE_ALLERGIC_INGREDIENTS, null, values)
                if (result != -1L) {
                    Log.d("DBHelper", "Successfully added allergic ingredient mapping: userId=$userId, ingredientId=$ingredientId")
                    notifyAllergyChanged()
                    true
                } else {
                    Log.e("DBHelper", "Failed to add allergic ingredient mapping to DB (insert returned -1).")
                    false
                }
            }
        } finally {
            db.close()
        }
    }

    fun removeAllergicIngredient(userId: Int, ingredientId: Int): Boolean {
        val db = writableDatabase
        return try {
            if (!isIngredientAllergicInternal(db, userId, ingredientId)) {
                Log.d("DBHelper", "User $userId is not allergic to ingredient $ingredientId. No removal needed.")
                true
            } else {
                val whereClause = "$COLUMN_USER_ID = ? AND $COLUMN_ID_INGREDIENT = ?"
                val whereArgs = arrayOf(userId.toString(), ingredientId.toString())
                val rowsDeleted = db.delete(TABLE_ALLERGIC_INGREDIENTS, whereClause, whereArgs)
                if (rowsDeleted > 0) {
                    Log.d("DBHelper", "Successfully removed allergic ingredient mapping for userId=$userId, ingredientId=$ingredientId")
                    notifyAllergyChanged()
                    true
                } else {
                    Log.w("DBHelper", "No allergic ingredient mapping found to remove for userId=$userId, ingredientId=$ingredientId")
                    false
                }
            }
        } finally {
            db.close()
        }
    }

    fun isIngredientAllergic(userId: Int, ingredientId: Int): Boolean {
        val db = readableDatabase
        return try {
            isIngredientAllergicInternal(db, userId, ingredientId)
        } finally {
            db.close()
        }
    }

    fun getAllergicIngredients(userId: Int): List<String> {
        val db = readableDatabase
        val query = """
            SELECT i.$COLUMN_INGREDIENT_NAME 
            FROM $TABLE_INGREDIENTS i
            INNER JOIN $TABLE_ALLERGIC_INGREDIENTS a 
            ON i.$COLUMN_INGREDIENT_ID = a.$COLUMN_ID_INGREDIENT
            WHERE a.$COLUMN_USER_ID = ?
        """.trimIndent()
        var cursor: Cursor? = null
        val list = mutableListOf<String>()
        try {
            cursor = db.rawQuery(query, arrayOf(userId.toString()))
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME)
                do {
                    val name = cursor.getString(nameIndex)
                    list.add(name)
                } while (cursor.moveToNext())
            } else {
                Log.d("DBHelper", "No allergic ingredients found for userId=$userId")
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "Error fetching allergic ingredients for userId=$userId: ${e.message}", e)
        } finally {
            cursor?.close()
            db.close()
        }
        return list
    }

    fun getAllergicIngredientsFlow(userId: Int): Flow<List<String>> {
        return _allergyChanges.flatMapLatest {
            flow {
                val ingredients = withContext(Dispatchers.IO) {
                    getAllergicIngredients(userId)
                }
                emit(ingredients)
            }
        }.distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }
}