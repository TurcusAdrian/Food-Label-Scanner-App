package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.food_label_scanner.R
import com.example.food_label_scanner.data.SettingsItem
import com.example.food_label_scanner.ui.theme.Cream

data class MenuItem(val title: String, val icon: Int)


@Composable
fun Settings(navController: NavHostController) {

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream) // Dark background
            .padding(16.dp)
    ) {

        val menuItems = listOf(
            MenuItem("Notifications", R.drawable.notification_icon),
            MenuItem("Privacy Policy", R.drawable.privacy_policy_icon),
            MenuItem("Terms of Service", R.drawable.terms_of_service_icon),
            MenuItem("Support", R.drawable.support_icon)
        )

        // Menu items
        menuItems.forEach { menuItem ->
            SettingsItem(title = menuItem.title, icon = menuItem.icon){when (menuItem.title) {
                "Notifications" -> navController.navigate("Notifications")
                "Privacy Policy" -> navController.navigate("PrivacyPolicy")
                "Terms of Service" -> navController.navigate("TermsOfService")
                "Support" -> navController.navigate("Support")
            }}
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}