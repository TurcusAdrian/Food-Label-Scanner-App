package com.example.food_label_scanner.database

import android.util.Log
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

@HiltViewModel
class IngredientDetailsViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val dbHelper: DBHelper
) : ViewModel() {

    private val _ingredient = MutableStateFlow<Ingredient?>(null)
    val ingredient: StateFlow<Ingredient?> = _ingredient.asStateFlow()

    private val _categoryName = MutableStateFlow<String?>(null)
    val categoryName: StateFlow<String?> = _categoryName.asStateFlow()

    private val _isAllergic = MutableStateFlow(false)
    val isAllergic: StateFlow<Boolean> = _isAllergic.asStateFlow()

    private val _allergicIngredients = MutableStateFlow<List<String>>(emptyList())

    fun loadInitialData(ingredientId: Int, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val allIngredients = ingredientRepository.getAllIngredients()
            val foundIngredient = allIngredients.firstOrNull { it.ingredient_id == ingredientId }
            _ingredient.value = foundIngredient

            foundIngredient?.let {
                _categoryName.value = dbHelper.getCategoryNameById(it.category_id)
            }
            if (userId != -1) {
                _isAllergic.value = dbHelper.isIngredientAllergic(userId, ingredientId)
                _allergicIngredients.value = dbHelper.getAllergicIngredients(userId)
            }
        }
    }

    fun toggleAllergicStatus(userId: Int, ingredientId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("ViewModel", "Toggling allergic status for userId=$userId, ingredientId=$ingredientId")
            val currentlyAllergic = _isAllergic.value
            if (currentlyAllergic) {
                Log.d("ViewModel", "Removing allergic ingredient")
                dbHelper.removeAllergicIngredient(userId, ingredientId)
            } else {
                Log.d("ViewModel", "Adding allergic ingredient")
                val success = dbHelper.addAllergicIngredient(userId, ingredientId)
                Log.d("ViewModel", "Add operation success: $success")
                if (!success) {
                    Log.e("ViewModel", "Failed to add allergic ingredient")
                    return@launch // Exit if add fails
                }
            }
            _isAllergic.value = !currentlyAllergic
            val updatedAllergicIngredients = dbHelper.getAllergicIngredients(userId)
            Log.d("ViewModel", "Updated allergic ingredients: $updatedAllergicIngredients")
            _allergicIngredients.value = updatedAllergicIngredients
        }
    }
}