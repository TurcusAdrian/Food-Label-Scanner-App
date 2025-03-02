package com.example.food_label_scanner.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IngredientDetailsViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository
) : ViewModel() {

    private val _ingredient = MutableStateFlow<Ingredient?>(null)
    val ingredient: StateFlow<Ingredient?> = _ingredient.asStateFlow()

    fun loadIngredient(ingredientId: Int) {
        viewModelScope.launch {
            val allIngredients = ingredientRepository.getAllIngredients()
            _ingredient.value = allIngredients.firstOrNull { it.ingredient_id == ingredientId }
        }
    }
}