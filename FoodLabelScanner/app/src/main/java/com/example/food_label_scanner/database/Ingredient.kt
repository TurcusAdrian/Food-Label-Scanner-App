package com.example.food_label_scanner.database

data class Ingredient(
    val ingredient_id : Int,
    val name : String,
    val nutritional_value : String,
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

