package com.example.food_label_scanner.bottom_bar_drawer_content_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.food_label_scanner.R
import com.example.food_label_scanner.data.SettingsItem

data class MenuItem(val title: String, val icon: Int)


@Composable
fun Settings(navController: NavHostController) {
    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Dark background
            .padding(16.dp)
    ) {

        val menuItems = listOf(
            MenuItem("Blocked Accounts", R.drawable.blocked_accounts_icon), //Block
            MenuItem("Notifications", R.drawable.notification_icon),
            MenuItem("Privacy Policy", R.drawable.privacy_policy_icon), //PrivacyTip
            MenuItem("Terms of Service", R.drawable.terms_of_service_icon), //Article
            MenuItem("Community Guidelines", R.drawable.community_guidelines_icon), //Rule
            MenuItem("Support", R.drawable.support_icon) //Help
        )

        // Menu items
        menuItems.forEach { menuItem ->
            SettingsItem(title = menuItem.title, icon = menuItem.icon){when (menuItem.title) {
                "Blocked Accounts" -> navController.navigate("blocked_accounts")
                "Notifications" -> navController.navigate("notifications")
                "Privacy Policy" -> navController.navigate("privacy_policy")
                "Terms of Service" -> navController.navigate("terms_of_service")
                "Community Guidelines" -> navController.navigate("community_guidelines")
                "Support" -> navController.navigate("Support")
            }}
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}