package com.example.food_label_scanner.search_bar_functionality

import android.content.Context
import android.content.SharedPreferences
import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.food_label_scanner.DBHelper
import com.example.food_label_scanner.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.remove

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository // Injected instance
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())

    private val _searchHistory = MutableStateFlow<List<Ingredient>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()
    private val historySize = 3

    val ingredients = searchText.combine(_ingredients) { text, ingredients ->
        if (text.isBlank()) {
            ingredients
        } else {
            ingredients.filter { it.doesItMatchSearchQuery(text) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _ingredients.value)

    init {
        viewModelScope.launch {
            // Fetch ingredients from the database when the ViewModel is created
            _ingredients.value = ingredientRepository.getAllIngredients() // Correctly using the instance
        }

        viewModelScope.launch {
            searchText.collect { text ->
                if (text.isNotBlank()) {
                    val matchingIngredient = _ingredients.value.firstOrNull { it.doesItMatchSearchQuery(text) }
                    if (matchingIngredient != null) {
                        _searchHistory.value = (_searchHistory.value + matchingIngredient)
                            .distinct()
                            .takeLast(historySize)
                    }
                }
            }
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}
