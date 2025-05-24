package com.example.food_label_scanner.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_label_scanner.DBHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
// ... other imports

@HiltViewModel
class AllergicIngredientsViewModel @Inject constructor(
    private val dbHelper: DBHelper // Inject DBHelper (or a Repository)
) : ViewModel() {

    // Assuming you get userId similarly or it's passed/known
    // For this example, let's assume it's fixed or obtained.
    // In a real app, you might get this from a shared service or saved preferences.
    private val userIdFlow = MutableStateFlow<Int>(-1) // Placeholder for userId

    fun setUserId(id: Int) {
        userIdFlow.value = id
    }

    val allergicIngredients: StateFlow<List<String>> = userIdFlow.flatMapLatest { currentUserId ->
        if (currentUserId != -1) {
            dbHelper.getAllergicIngredientsFlow(currentUserId)
        } else {
            flowOf(emptyList()) // Return empty list if no valid userId
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep subscribed for 5s after last collector
        initialValue = emptyList()
    )

    // No explicit loadAllergicIngredients needed if using the flow correctly
    // The flow will automatically update when the database changes (due to notifyAllergyChanged)
    // and when userIdFlow changes.
}