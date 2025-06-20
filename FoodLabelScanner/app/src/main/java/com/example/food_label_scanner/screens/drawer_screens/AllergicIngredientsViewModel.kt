package com.example.food_label_scanner.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_label_scanner.DBHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn


@HiltViewModel
class AllergicIngredientsViewModel @Inject constructor(
    private val dbHelper: DBHelper
) : ViewModel() {


    private val userIdFlow = MutableStateFlow<Int>(-1)

    fun setUserId(id: Int) {
        userIdFlow.value = id
    }

    val allergicIngredients: StateFlow<List<String>> = userIdFlow.flatMapLatest { currentUserId ->
        if (currentUserId != -1) {
            dbHelper.getAllergicIngredientsFlow(currentUserId)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

}
