package com.example.food_label_scanner.screens

sealed class Screens(val screen: String) {

    //bottom bar screens:
    data object Home: Screens("Home")
    data object Search: Screens("Search")
    data object Favourites: Screens("FavouritesItems")

    //Drawer screens:
    data object Account: Screens("Account")
    data object SearchHistory: Screens("SearchHistory") //to be implemented
    data object BarcodeScanning: Screens("BarcodeScanning") //to be implemented
    data object Settings: Screens("Settings")
    data object HowToUse: Screens("HowToUse")
    data object About: Screens("About")

    //Settings screen screens:
    data object Notifications: Screens("Notifications")
    data object PrivacyPolicy: Screens("PrivacyPolicy")
    data object TermsOfService: Screens("TermsOfService")
    data object CommunityGuidelines: Screens("CommunityGuidelines")
    data object Support: Screens("Support")

    data object IngredientDetails : Screens("ingredient_details_screen/{ingredientId}") {
        fun createRoute(ingredientId: Int) = "ingredient_details_screen/$ingredientId"
    }

    data object BarcodeDisplay : Screens("barcodeDisplay/{barcode}") {
        fun createRoute(barcode: String) = "barcodeDisplay/$barcode"
    }
}