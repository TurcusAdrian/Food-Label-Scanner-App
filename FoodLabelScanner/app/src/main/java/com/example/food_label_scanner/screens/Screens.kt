package com.example.food_label_scanner.screens

sealed class Screens(val screen: String) {

    //bottom bar screens:
    data object Home: Screens("Home")
    data object Search: Screens("Search")
    data object Favourites: Screens("FavouritesItems")

    //Drawer screens:
    data object Account: Screens("Account") //to be implemented
    data object SearchHistory: Screens("SearchHistory") //to be implemented
    data object Friends: Screens("Friends") //to be implemented
    data object Settings: Screens("Settings")
    data object About: Screens("About")

    //Settings screen screens:
    data object BlockedAccounts: Screens("BlockedAccounts")
    data object Notification: Screens("Notifications")
    data object PrivacyPolicy: Screens("PrivacyPolicy")
    data object TermsOfService: Screens("TermsOfService")
    data object CommunityGuidelines: Screens("CommunityGuidelines")
    data object Support: Screens("Support")

}