package com.example.food_label_scanner.bottom_bar_screens

sealed class Screens(val screen: String) {
    data object Home: Screens("Home")
    data object Search: Screens("Search")
    data object Favourites: Screens("FavouritesItems")
}