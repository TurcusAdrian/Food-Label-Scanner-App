package com.example.food_label_scanner.bottom_bar_drawer_content_screens

sealed class Screens(val screen: String) {

    //bottom bar screens:

    data object Home: Screens("Home")
    data object Search: Screens("Search")
    data object TakePictures: Screens("TakePicture")
    data object Favourites: Screens("FavouritesItems")

    //Drawer screens:
    data object Settings: Screens("Settings")
    data object About: Screens("About")
}