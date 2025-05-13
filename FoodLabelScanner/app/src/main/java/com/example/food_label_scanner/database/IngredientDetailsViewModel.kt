package com.example.food_label_scanner.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_label_scanner.DBHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientDetailsViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val dbHelper: DBHelper // Inject DBHelper here
) : ViewModel() {

    private val _ingredient = MutableStateFlow<Ingredient?>(null)
    val ingredient: StateFlow<Ingredient?> = _ingredient.asStateFlow()

    private val _categoryName = MutableStateFlow<String?>(null)
    val categoryName: StateFlow<String?> = _categoryName.asStateFlow()

    fun loadIngredient(ingredientId: Int) {
        viewModelScope.launch {
            val allIngredients = ingredientRepository.getAllIngredients()
            val foundIngredient = allIngredients.firstOrNull { it.ingredient_id == ingredientId }

            _ingredient.value = foundIngredient // Update the ingredient StateFlow

            if (foundIngredient != null) {
                // Fetch the category name using the category_id from the found ingredient
                val fetchedCategoryName = dbHelper.getCategoryNameById(foundIngredient.category_id)
                _categoryName.value = fetchedCategoryName // Update the categoryName StateFlow
            } else {
                _categoryName.value = null // Clear category name if ingredient not found
            }
        }
    }
}