package com.example.food_label_scanner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class DBHelper @Inject constructor(
    @ApplicationContext context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 2 // Increment the version

        //table for storing individual users
        private const val TABLE_USERS = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        //table for relation between users
        private const val TABLE_FRIENDSHIPS = "user_friendships"
        private const val COLUMN_USER_ID_1 = "user_id_1"
        private const val COLUMN_USER_ID_2 = "user_id_2"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_CREATED_AT = "created_at"
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

            // Create user_friendships table
            val createFriendshipsTableQuery = ("CREATE TABLE $TABLE_FRIENDSHIPS (" +
                    "$COLUMN_USER_ID_1 INTEGER, " +
                    "$COLUMN_USER_ID_2 INTEGER, " +
                    "$COLUMN_STATUS TEXT, " +
                    "$COLUMN_CREATED_AT DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "PRIMARY KEY ($COLUMN_USER_ID_1, $COLUMN_USER_ID_2), " +
                    "FOREIGN KEY ($COLUMN_USER_ID_1) REFERENCES $TABLE_USERS($COLUMN_ID), " +
                    "FOREIGN KEY ($COLUMN_USER_ID_2) REFERENCES $TABLE_USERS($COLUMN_ID))")
            db?.execSQL(createFriendshipsTableQuery)
            Log.d("DBHelper", "Friendships table created")
        } catch (e: Exception) {
            Log.e("DBHelper", "Error creating tables", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d("DBHelper", "onUpgrade called from $oldVersion to $newVersion")
        val dropUsersTableQuery = "DROP TABLE IF EXISTS $TABLE_USERS"
        val dropFriendshipsTableQuery = "DROP TABLE IF EXISTS $TABLE_FRIENDSHIPS"
        db?.execSQL(dropUsersTableQuery)
        db?.execSQL(dropFriendshipsTableQuery)
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

    // Friendship Table Methods
    fun createFriendship(user_id_1: Int, user_id_2: Int, status: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USER_ID_1, user_id_1)
            put(COLUMN_USER_ID_2, user_id_2)
            put(COLUMN_STATUS, status)
        }
        val db = writableDatabase
        return db.insert(TABLE_FRIENDSHIPS, null, values)
    }

    fun updateFriendshipStatus(user_id_1: Int, user_id_2: Int, status: String): Int {
        val values = ContentValues().apply {
            put(COLUMN_STATUS, status)
        }
        val db = writableDatabase
        val selection = "$COLUMN_USER_ID_1 = ? AND $COLUMN_USER_ID_2 = ?"
        val selectionArgs = arrayOf(user_id_1.toString(), user_id_2.toString())
        return db.update(TABLE_FRIENDSHIPS, values, selection, selectionArgs)
    }

    fun getFriendshipStatus(user_id_1: Int, user_id_2: Int): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USER_ID_1 = ? AND $COLUMN_USER_ID_2 = ?"
        val selectionArgs = arrayOf(user_id_1.toString(), user_id_2.toString())
        val cursor = db.query(
            TABLE_FRIENDSHIPS,
            arrayOf(COLUMN_STATUS),
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var status: String? = null
        if (cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
        }
        cursor.close()
        return status
    }

    fun getFriendshipsForUser(user_id: Int): Cursor {
        val db = readableDatabase
        val selection = "$COLUMN_USER_ID_1 = ? OR $COLUMN_USER_ID_2 = ?"
        val selectionArgs = arrayOf(user_id.toString(), user_id.toString())
        return db.query(TABLE_FRIENDSHIPS, null, selection, selectionArgs, null, null, null)
    }

    fun getBlockedAccountsForUser(userId: Int): List<String> {
        val db = readableDatabase
        val query = """
        SELECT $TABLE_USERS.$COLUMN_USERNAME FROM $TABLE_USERS 
        INNER JOIN $TABLE_FRIENDSHIPS 
        ON $TABLE_USERS.$COLUMN_ID = $TABLE_FRIENDSHIPS.$COLUMN_USER_ID_2
        WHERE $TABLE_FRIENDSHIPS.$COLUMN_USER_ID_1 = ? 
        AND $TABLE_FRIENDSHIPS.$COLUMN_STATUS = 'blocked'
    """
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        val blockedAccounts = mutableListOf<String>()
        try {
            if (cursor.moveToFirst()) {
                do {
                    blockedAccounts.add(cursor.getString(0)) // Get username
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            // Log any error that occurs
            Log.e("DBHelper", "Error retrieving blocked accounts for userId: $userId", e)
        } finally {
            // Always close the cursor to avoid memory leaks
            cursor.close()
        }
        return blockedAccounts
    }

    fun blockUser(currentUserId: Int, blockedUserId: Int): Long {
        val db = writableDatabase

        // Check if the friendship already exists
        val query = """
        SELECT * FROM $TABLE_FRIENDSHIPS 
        WHERE $COLUMN_USER_ID_1 = ? AND $COLUMN_USER_ID_2 = ?
    """
        val cursor = db.rawQuery(query, arrayOf(currentUserId.toString(), blockedUserId.toString()))
        return try {
            if (cursor.moveToFirst()) {
                // Update status to "blocked" if friendship exists
                val values = ContentValues().apply {
                    put(COLUMN_STATUS, "blocked")
                }
                db.update(
                    TABLE_FRIENDSHIPS,
                    values,
                    "$COLUMN_USER_ID_1 = ? AND $COLUMN_USER_ID_2 = ?",
                    arrayOf(currentUserId.toString(), blockedUserId.toString())
                ).toLong()
            } else {
                // Insert new record with "blocked" status if no friendship exists
                val values = ContentValues().apply {
                    put(COLUMN_USER_ID_1, currentUserId)
                    put(COLUMN_USER_ID_2, blockedUserId)
                    put(COLUMN_STATUS, "blocked")
                }
                db.insert(TABLE_FRIENDSHIPS, null, values)
            }
        } catch (e: Exception) {
            Log.e("DBHelper", "Error blocking user $blockedUserId for $currentUserId", e)
            -1L
        } finally {
            cursor.close()
        }
    }

    fun unblockUser(currentUserId: Int, blockedUserId: Int): Int {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COLUMN_STATUS, "unblocked") // or you can delete the entry if needed
            }
            db.update(
                TABLE_FRIENDSHIPS,
                values,
                "$COLUMN_USER_ID_1 = ? AND $COLUMN_USER_ID_2 = ?",
                arrayOf(currentUserId.toString(), blockedUserId.toString())
            )
        } catch (e: Exception) {
            Log.e("DBHelper", "Error unblocking user $blockedUserId for $currentUserId", e)
            -1
        }
    }

}