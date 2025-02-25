package com.example.food_label_scanner


import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class FriendsViewModel @Inject constructor(private val dbHelper: DBHelper) : ViewModel() {

    private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val friendRequests: StateFlow<List<FriendRequest>> = _friendRequests

    private val _friendsList = MutableStateFlow<List<String>>(emptyList())
    val friendsList: StateFlow<List<String>> = _friendsList

    // Assume current user ID is 1 for simplicity
    private var currentUserId: Int = 1


    init {
        loadCurrentUserId()
        loadFriendRequests() // Load requests when the view model initializes
        loadFriendsList() // Load friends list for the current user
    }

    private fun loadCurrentUserId() {
        // Retrieve the current user ID from SharedPreferences or wherever it's stored
        // For this example, I'm using a hardcoded value, but you should replace this with your actual logic
        // Example:
         //val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        //currentUserId = sharedPref.getInt("userId", 1)

    }

    fun sendFriendRequest(username: String) {
        Log.d("FriendsViewModel", "Sending friend request to: $username")
        val recipientId = dbHelper.getUserId(username)
        Log.d("FriendsViewModel", "Recipient ID: $recipientId")
        if (recipientId != -1) {
            // Check if the user is trying to send a request to themselves
            if (recipientId == currentUserId) {
                Log.d("FriendsViewModel", "Cannot send friend request to yourself.")
                return // Do not send request if it's to the same user
            }
            // Create friendship with "pending" status
            dbHelper.createFriendship(currentUserId, recipientId, "pending")
            notifyRecipientOfNewRequest(recipientId) // Notify recipient to refresh their requests
        }
    }

    private fun notifyRecipientOfNewRequest(recipientId: Int) {
        // This function forces a refresh for the recipient's side
        // Get the friend requests for the recipient and update their list
        val cursor = dbHelper.getFriendRequestsForUser(recipientId)
        val requests = mutableListOf<FriendRequest>()
        while (cursor.moveToNext()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            requests.add(FriendRequest(username))
        }
        cursor.close()
        // Update recipient's friend request list
        _friendRequests.value = requests
    }

    fun acceptFriendRequest(username: String) {
        val requesterId = dbHelper.getUserId(username)
        if (requesterId != -1) {
            val updatedRows = dbHelper.updateFriendshipStatus(requesterId, currentUserId, "accepted")
            if (updatedRows > 0) {
                loadFriendRequests() // Refresh friend requests after acceptance
                loadFriendsList()   // Refresh friends list after acceptance
            }
        }
    }

    fun rejectFriendRequest(username: String) {
        val requesterId = dbHelper.getUserId(username)
        if (requesterId != -1) {
            dbHelper.updateFriendshipStatus(requesterId, currentUserId, "rejected")
            loadFriendRequests() // Refresh the friend requests after rejection
        }
    }

    private fun loadFriendRequests() {
        val cursor = dbHelper.getFriendRequestsForUser(currentUserId)
        Log.d("FriendsViewModel", "Friend requests cursor: $cursor")
        val requests = mutableListOf<FriendRequest>()
        while (cursor.moveToNext()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            requests.add(FriendRequest(username))
        }
        cursor.close()
        _friendRequests.value = requests // Update state with new friend requests
        Log.d("FriendsViewModel", "Friend requests: $requests")
    }

    private fun loadFriendsList() {
        val cursor = dbHelper.getFriendsForUser(currentUserId)
        val friends = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val username = cursor.getString(cursor.getColumnIndexOrThrow("username"))
            friends.add(username)
        }
        cursor.close()
        _friendsList.value = friends // Update state with new friends list
    }
}

data class FriendRequest(val username: String)