package com.example.food_label_scanner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHelper(private val context:Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1

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
        // Create users table
        val createUsersTableQuery = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(createUsersTableQuery)

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
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
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
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_ID), selection, selectionArgs, null, null, null)
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
        val cursor = db.query(TABLE_FRIENDSHIPS, arrayOf(COLUMN_STATUS), selection, selectionArgs, null, null, null)
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
}