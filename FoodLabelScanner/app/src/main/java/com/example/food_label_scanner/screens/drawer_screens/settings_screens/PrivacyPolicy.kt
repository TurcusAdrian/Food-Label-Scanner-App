package com.example.food_label_scanner.screens.drawer_screens.settings_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.food_label_scanner.ui.theme.Cream
import com.example.food_label_scanner.ui.theme.Teal2

@Composable
fun PrivacyPolicy(navController: NavHostController) {
    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(16.dp)
        ) {
            Text(
                text = "Privacy Policy",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontFamily = instrument_serif
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Teal2
            )) {
                Text(
                    text = longPrivacyPolicyText,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = instrument_serif,
                        textAlign = TextAlign.Justify
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                        .background(Teal2)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Back")
            }
        }
    }
}

val longPrivacyPolicyText = """
    Privacy Policy – Food Label Scanner App

    Effective Date: June 7, 2025

    Your privacy is important to us. This Privacy Policy explains how we collect, use, and protect your information when you use the Food Label Scanner app.

    1. Information We Collect
    - **Scanned Data**: When you scan food product barcodes or take photos of ingredient lists, we may extract and analyze textual information such as product names, ingredients, and nutrition facts. This data is processed locally on your device and/or using trusted APIs (e.g., Open Food Facts or MLKit).
    - **User Preferences**: We may store information like your saved favorite ingredients, allergens, or dietary preferences for personalized recommendations.
    - **Device Information**: We collect non-personal data such as device type, app version, and crash logs for performance and analytics purposes.

    2. How We Use Your Information
    - To identify food products and ingredients based on barcode or text input.
    - To provide ingredient descriptions, health ratings, and dietary insights.
    - To store and recall user preferences like allergies or favorite items.
    - To improve the accuracy, speed, and usability of the app through anonymous analytics.

    3. Data Storage and Security
    - Your data is stored locally on your device using secure methods (e.g., SQLite).
    - We do not sell, rent, or share your personal data with third parties.
    - If cloud features are added in the future (e.g., user accounts), this policy will be updated to reflect how that data is handled.

    4. Permissions
    - The app may request camera access to enable barcode scanning.
    - Photo storage access may be required to analyze saved images (optional).
    - All permissions are optional and explained at runtime.

    5. Third-Party Services
    - We may use APIs like Open Food Facts for publicly available food data.
    - Text recognition and language processing features may use services from Google MLKit or other providers.
    - These third-party services may have their own privacy policies.

    6. Children's Privacy
    - This app is not intended for children under the age of 13. We do not knowingly collect personal data from children.

    7. Your Rights
    - You may request deletion of your saved data at any time by clearing the app's local storage via your device settings.
    - You can revoke permissions through your device's permission manager.

    8. Updates to This Policy
    - This policy may be updated to reflect changes in the app’s functionality or applicable laws. Users will be notified of major changes within the app interface or via app store updates.

    9. Contact
    - For any questions about this policy or your data, contact us at: support@foodlabelscannerapp.com

    By using the Food Label Scanner app, you agree to this Privacy Policy.
""".trimIndent()