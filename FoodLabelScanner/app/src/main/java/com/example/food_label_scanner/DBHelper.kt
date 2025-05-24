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

        // Table for allergic ingredients
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
    private val mutex = Mutex()



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
            /*
            //create alergic table
            val createAllergicTable = ("CREATE TABLE  $TABLE_ALLERGIC_INGREDIENTS (" +
                            "$COLUMN_USER_ID INTEGER FOREIGN KEY, " +
                            "$COLUMN_ID_INGREDIENT INTEGER," +
                            "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_ID_INGREDIENT + ")," +
                            "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES $TABLE_USERS($COLUMN_ID) + // Assuming a users table
                            "FOREIGN KEY (" + COLUMN_ID_INGREDIENT + ") REFERENCES $TABLE_INGREDIENTS($COLUMN_INGREDIENT_ID)" +
                            ")"
                    ) */
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
        val dropAllergicTableQuery = "DROP TABLE IF EXISTS $TABLE_ALLERGIC_INGREDIENTS"

        db?.execSQL(dropIngredientsTableQuery)
        db?.execSQL(dropUsersTableQuery)
        db?.execSQL(dropAllergicTableQuery)
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
    private suspend fun <T> withDatabase(isTransaction: Boolean = false, block: suspend (SQLiteDatabase) -> T): T {
        return mutex.withLock { // Ensures only one transaction/write operation happens at a time
            val db = writableDatabase
            if (isTransaction) {
                Log.d("DBHelper", "Beginning transaction for block: ${block::class.simpleName}")
                db.beginTransaction()
            }
            try {
                val result = block(db) // block is suspend, can call other suspend functions
                // that DON'T try to re-acquire this specific db via withDatabase/withReadableDatabase
                if (isTransaction) {
                    Log.d("DBHelper", "Setting transaction successful for block: ${block::class.simpleName}")
                    db.setTransactionSuccessful()
                }
                result
            } catch (e: Exception) { // Catch generic Exception to be safe, though SQLiteException is common
                Log.e("DBHelper", "Error during database operation (transaction: $isTransaction): ${e.message}", e)
                throw e // Re-throw so the caller (ViewModel) can potentially handle it
            } finally {
                if (isTransaction) {
                    Log.d("DBHelper", "Ending transaction for block: ${block::class.simpleName}")
                    db.endTransaction()
                }
                // Do NOT close 'db' here; SQLiteOpenHelper manages its lifecycle.
            }
        }
    }

    private suspend fun <T> withReadableDatabase(block: suspend (SQLiteDatabase) -> T): T {
        return mutex.withLock { // Mutex can still be useful to serialize even reads if they are complex
            // or if WAL mode isn't enabled, though less critical than for writes.
            val db = readableDatabase
            try {
                block(db)
            } catch (e: SQLiteException) {
                Log.e("DBHelper", "SQLite error in withReadableDatabase: ${e.message}", e)
                throw e
            }
        }
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
        }finally {
            db.close() // Close DB in finally
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


    private val _allergyChanges = MutableSharedFlow<Unit>(replay = 1)
    init {
        _allergyChanges.tryEmit(Unit) // Initial emit
    }

    // Call this after add/remove in DBHelper
    private fun notifyAllergyChanged() {
        _allergyChanges.tryEmit(Unit)
    }



    // These are now regular functions that operate on a given db instance.
// They are intended to be called from within a withDatabase/withReadableDatabase block.
    private fun doesUserExistInternal(db: SQLiteDatabase, userId: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_USERS WHERE $COLUMN_ID = ?",
            arrayOf(userId.toString())
        )
        try {
            return cursor.moveToFirst()
        } finally {
            cursor.close()
        }
    }

    private fun doesIngredientExistInternal(db: SQLiteDatabase, ingredientId: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_INGREDIENTS WHERE $COLUMN_INGREDIENT_ID = ?",
            arrayOf(ingredientId.toString())
        )
        try {
            return cursor.moveToFirst()
        } finally {
            cursor.close()
        }
    }

    private fun isIngredientAllergicInternal(db: SQLiteDatabase, userId: Int, ingredientId: Int): Boolean {
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_ALLERGIC_INGREDIENTS WHERE $COLUMN_USER_ID=? AND $COLUMN_ID_INGREDIENT=?",
            arrayOf(userId.toString(), ingredientId.toString())
        )
        try {
            Log.d("AllergyToggleInternal", "Checking if ingredient is allergic: userId=$userId, ingredientId=$ingredientId")
            return cursor.moveToFirst()
        } finally {
            cursor.close()
        }
    }





    suspend fun doesIngredientExist(ingredientId: Int): Boolean {
        return withReadableDatabase { db -> // Uses its own readable context
            doesIngredientExistInternal(db, ingredientId)
        }
    }

    suspend fun doesUserExist(userId: Int): Boolean {
        return withReadableDatabase { db -> // Uses its own readable context
            doesUserExistInternal(db, userId)
        }
    }

    suspend fun addAllergicIngredient(userId: Int, ingredientId: Int): Boolean {
        Log.d("DBHelper", "Attempting to add allergic ingredient for userId=$userId, ingredientId=$ingredientId")
        val success = withDatabase(isTransaction = true) { db ->
            // Use the 'Internal' non-suspend versions here, passing the 'db' from this transaction
            if (!doesUserExistInternal(db, userId)) {
                Log.e("DBHelper", "Add failed: User with ID $userId does not exist.")
                return@withDatabase false // Exits the block, transaction will be rolled back
            }

            if (!doesIngredientExistInternal(db, ingredientId)) {
                Log.e("DBHelper", "Add failed: Ingredient with ID $ingredientId does not exist.")
                return@withDatabase false
            }

            if (isIngredientAllergicInternal(db, userId, ingredientId)) {
                Log.d("DBHelper", "User $userId is already allergic to ingredient $ingredientId. No change made.")
                // Return true because the desired state (being allergic) is met.
                // If you only want to return true on an actual DB write, this would be false.
                return@withDatabase true
            }

            val values = ContentValues().apply {
                put(COLUMN_USER_ID, userId)
                put(COLUMN_ID_INGREDIENT, ingredientId) // Assuming this is the correct column name
            }

            val result = db.insert(TABLE_ALLERGIC_INGREDIENTS, null, values)
            if (result != -1L) {
                Log.d("DBHelper", "Successfully added allergic ingredient mapping: userId=$userId, ingredientId=$ingredientId. Result ID: $result")
                true
            } else {
                Log.e("DBHelper", "Failed to add allergic ingredient mapping to DB (insert returned -1).")
                false
            }
        }

        if (success) {
            Log.d("DBHelper", "Notifying allergy change after successful add.")
            notifyAllergyChanged() // Notify observers that the allergy list might have changed
        }
        return success
    }


    suspend fun removeAllergicIngredient(userId: Int, ingredientId: Int): Boolean {
        Log.d("DBHelper", "Attempting to remove allergic ingredient for userId=$userId, ingredientId=$ingredientId")
        var actuallyRemoved = false // To track if a DB change occurred for notification
        val success = withDatabase(isTransaction = true) { db ->
            // Optional: Check if the user and ingredient exist before attempting delete,
            // though delete itself won't fail if the row isn't there (it will just delete 0 rows).
            // For consistency with 'add', you might add these checks.
            // if (!doesUserExistInternal(db, userId)) { ... return@withDatabase false }
            // if (!doesIngredientExistInternal(db, ingredientId)) { ... return@withDatabase false }

            // Check if the allergy link actually exists before trying to delete
            // This helps in deciding whether to call notifyAllergyChanged()
            if (!isIngredientAllergicInternal(db, userId, ingredientId)) {
                Log.d("DBHelper", "User $userId is not allergic to ingredient $ingredientId. No removal needed.")
                return@withDatabase true // Or false if you consider "no operation" as not successful for removal
            }

            val whereClause = "$COLUMN_USER_ID = ? AND $COLUMN_ID_INGREDIENT = ?"
            val whereArgs = arrayOf(userId.toString(), ingredientId.toString())

            val rowsDeleted = db.delete(
                TABLE_ALLERGIC_INGREDIENTS,
                whereClause,
                whereArgs
            )

            if (rowsDeleted > 0) {
                Log.d("DBHelper", "Successfully removed $rowsDeleted allergic ingredient mapping(s) for userId=$userId, ingredientId=$ingredientId.")
                actuallyRemoved = true
                true // Deletion was successful
            } else {
                // This case might be hit if isIngredientAllergicInternal was true, but by the time delete runs,
                // it's gone (race condition if not properly serialized, though `withDatabase` mutex helps).
                // Or if the initial check was skipped.
                Log.w("DBHelper", "No allergic ingredient mapping found to remove for userId=$userId, ingredientId=$ingredientId (rowsDeleted=0).")
                false // No rows were deleted
            }
        }

        // Only notify if a change was actually made to the database state
        if (actuallyRemoved) {
            Log.d("DBHelper", "Notifying allergy change after successful removal.")
            notifyAllergyChanged()
        }
        return success // Return the overall success of the operation as defined by the transaction block
    }


    suspend fun isIngredientAllergic(userId: Int, ingredientId: Int): Boolean {
        return withReadableDatabase { db -> // Uses its own readable context
            isIngredientAllergicInternal(db, userId, ingredientId)
        }
    }


    suspend fun getAllergicIngredients(userId: Int): List<String> {
        return withReadableDatabase { db -> // Uses the withReadableDatabase helper
            val query = """
            SELECT i.$COLUMN_INGREDIENT_NAME 
            FROM $TABLE_INGREDIENTS i
            INNER JOIN $TABLE_ALLERGIC_INGREDIENTS a 
            ON i.$COLUMN_INGREDIENT_ID = a.$COLUMN_ID_INGREDIENT
            WHERE a.$COLUMN_USER_ID = ?
        """.trimIndent()

            var cursor: Cursor? = null // Declare cursor outside try to ensure it's accessible in finally
            val list = mutableListOf<String>()
            try {
                cursor = db.rawQuery(query, arrayOf(userId.toString()))
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndexOrThrow(COLUMN_INGREDIENT_NAME) // Get index once, throw if not found
                    do {
                        val name = cursor.getString(nameIndex)
                        // Log.d("DBHelper", "Found allergic ingredient for userId=$userId: $name") // Uncomment for debugging if needed
                        list.add(name)
                    } while (cursor.moveToNext())
                } else {
                    Log.d("DBHelper", "No allergic ingredients found for userId=$userId")
                }
            } catch (e: Exception) { // Catch potential exceptions during query or cursor processing
                Log.e("DBHelper", "Error fetching allergic ingredients for userId=$userId: ${e.message}", e)
                // Optionally, rethrow or return empty list based on desired error handling
                // For now, it will return the (possibly empty) list accumulated so far, or an empty list if error was early.
            } finally {
                cursor?.close() // Ensure cursor is closed even if an exception occurs
            }
            list // Return the populated list (or empty list if none found or error occurred)
        }
    }

    fun getAllergicIngredientsFlow(userId: Int): Flow<List<String>> {
        return _allergyChanges.flatMapLatest { // Re-query when allergyChanges emits
            flow {
                // This is a simplified flow. In a real app, ensure this runs on Dispatchers.IO
                // and handles exceptions properly.
                val ingredients = withContext(Dispatchers.IO) { // Ensure DB access is off main thread
                    withReadableDatabase { db ->
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
                                    list.add(cursor.getString(nameIndex))
                                } while (cursor.moveToNext())
                            }
                        } finally {
                            cursor?.close()
                        }
                        list
                    }
                }
                emit(ingredients)
            }
        }.distinctUntilChanged() // Only emit if the list content actually changes
            .flowOn(Dispatchers.IO) // Ensure the upstream (including DB access) runs on IO dispatcher
    }



}
