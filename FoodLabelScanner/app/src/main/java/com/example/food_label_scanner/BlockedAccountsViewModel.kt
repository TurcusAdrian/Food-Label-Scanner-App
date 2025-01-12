package com.example.food_label_scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockedAccountsViewModel @Inject constructor(
    private val dbHelper: DBHelper
) : ViewModel() {

    private val _blockedAccounts = MutableStateFlow<List<String>>(emptyList())
    val blockedAccounts: StateFlow<List<String>> = _blockedAccounts.asStateFlow()

    private var currentUserId: Int = -1 // Set this based on the logged-in user

    fun setCurrentUser(userId: Int) {
        currentUserId = userId
        loadBlockedAccounts()
    }

    private fun loadBlockedAccounts() {
        viewModelScope.launch {
            _blockedAccounts.value = dbHelper.getBlockedAccountsForUser(currentUserId)
        }
    }

    fun blockUser(blockedUsername: String) {
        viewModelScope.launch {
            val blockedUserId = dbHelper.getUserId(blockedUsername)
            if (blockedUserId != -1) {
                dbHelper.blockUser(currentUserId, blockedUserId)
                loadBlockedAccounts()
            }
        }
    }

    fun unblockUser(blockedUsername: String) {
        viewModelScope.launch {
            val blockedUserId = dbHelper.getUserId(blockedUsername)
            if (blockedUserId != -1) {
                dbHelper.unblockUser(currentUserId, blockedUserId)
                loadBlockedAccounts()
            }
        }
    }
}