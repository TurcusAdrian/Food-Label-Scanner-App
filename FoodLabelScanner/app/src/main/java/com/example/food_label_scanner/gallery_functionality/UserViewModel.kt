package com.example.food_label_scanner.gallery_functionality

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _userId = MutableStateFlow<Int?>(null)
    val userId: StateFlow<Int?> = _userId

    init {
        val prefs = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedId = prefs.getInt("userId", -1)
        if (savedId != -1) {
            _userId.value = savedId
        }
    }

    fun setUserId(id: Int) {
        _userId.value = id
    }
}