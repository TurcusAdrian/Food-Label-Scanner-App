package com.example.food_label_scanner.screens.drawer_screens.settings_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.food_label_scanner.ui.theme.Cream
import com.example.food_label_scanner.ui.theme.Teal2

@Composable
fun TermsOfService(navController: NavHostController) {
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
                text = "Terms of Service",
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
                colors = CardDefaults.cardColors(
                    containerColor = Teal2
                )
            ) {
                Text(
                    text = termsOfServiceText,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = instrument_serif,
                        textAlign = TextAlign.Justify
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Decline")
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Accept")
                }
            }
        }
    }
}


val termsOfServiceText = """
    Terms of Service

    Welcome to the Food Label Scanner App. By using this application, you agree to comply with and be bound by the following Terms of Service. Please read these terms carefully.

    1. Acceptance of Terms
    By accessing or using the app, you agree to be bound by these Terms of Service and our Privacy Policy. If you do not agree, please do not use the app.

    2. App Purpose
    The Food Label Scanner App is intended to help users understand food ingredients by scanning product labels. It provides general nutritional information and ingredient analysis based on publicly available or user-provided data.

    3. No Medical Advice
    The information provided by this app is for educational and informational purposes only. It is not intended as medical advice. Always consult with a qualified health provider regarding dietary or health concerns.

    4. User Responsibilities
    - You are responsible for the accuracy of any information you input.
    - You agree not to misuse the app, including reverse engineering or attempting unauthorized access.

    5. Data and Privacy
    The app may store basic data such as your preferences, ingredient history, or favorite items locally on your device. Your data is not shared unless you explicitly allow it. For more details, refer to the Privacy Policy.

    6. Intellectual Property
    All content, logos, databases, and designs of this app are the property of the developers and protected by copyright and intellectual property laws. You may not reproduce, modify, or distribute any part of the app without prior written permission.

    7. Modifications and Updates
    We reserve the right to modify or update these Terms at any time. Continued use of the app after changes constitutes your acceptance of the new terms.

    8. Limitation of Liability
    The developers shall not be liable for any damages arising from the use or inability to use the app, including data loss or health consequences related to inaccurate ingredient interpretation.

    9. Termination
    We reserve the right to terminate or restrict your access to the app if you violate these terms or engage in misuse of the app.

    10. Governing Law
    These Terms of Service are governed by and construed in accordance with the laws of your local jurisdiction.

    Contact
    For any questions regarding these terms, please contact our support team at support@foodlabelscanner.com.

    Last Updated: June 2025

    Thank you for using the Food Label Scanner App.
""".trimIndent()
