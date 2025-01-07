package com.example.food_label_scanner.search_bar_functionality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _ingredients = MutableStateFlow(allingredients)

    val ingredients = searchText.combine(_ingredients){
                                                      text, ingredients -> if(text.isBlank()) { ingredients

    }else{ingredients.filter { it.doesItMatchSearchQuery(text) }}}
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _ingredients.value)

    fun onSearchTextChange(text : String){
        _searchText.value = text
    }
}

data class Ingredient(
    val ingredient_id : Int,
    val name : String,
    val nutritional_value : String,
    val category: String,
    val category_id : Int,
    val health_rating : Int,
    val description : String
){
    fun doesItMatchSearchQuery(query: String) : Boolean{
        val matchingCombination = listOf(
            "$name",
            "$name ${name.first()}",
            "${name.first()}${name.first()}",
            "${name.first()} ${name.first()}"
            )

        return matchingCombination.any{
            it.contains(query, ignoreCase = true)
        }
    }
}

val allingredients = listOf(

    Ingredient(1,
        "Zer",
        "5g proteine - 10g grasime",
        "1", 2,10,
        "Poate ingrasa in cantitati mari"),

    Ingredient(2,
        "Sirop de porumb",
        "10g grasimi", "3", 2,
        2,
        "Cauzator de diabet in cantitati mari"),

    Ingredient(3,
        "Zahar",
        "8g grasimi",
        "3", 2,
        2,
        "Cauzeaza diabet si ingrasare"),

    Ingredient(4,
    "Sare de mare",
    "380-400 mg sodiu",
    "1", 2,
    2,
    "Creste tensiunea in catitati mari")
)