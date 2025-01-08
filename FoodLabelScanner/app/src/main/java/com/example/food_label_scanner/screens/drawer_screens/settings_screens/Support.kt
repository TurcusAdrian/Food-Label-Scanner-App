package com.example.food_label_scanner.screens.drawer_screens.settings_screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier

@Composable
fun Support() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)) // Light background color
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // Spacing items from top to bottom
    ) {
        // Top content
        Column {
            // Title
            Text(
                text = "Help & Support",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Description
            Text(
                text = "If you are experiencing any issues, please let us know. We will try to solve them as soon as possible.",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Title input field
            OutlinedTextField(
                value = "", // You can manage the state here
                onValueChange = { /* Handle title input */ },
                label = { Text(text = "Title") },
                placeholder = { Text(text = "Add your grievance title here") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Problem input field
            OutlinedTextField(
                value = "", // You can manage the state here
                onValueChange = { /* Handle problem input */ },
                label = { Text(text = "Explain the problem") },
                placeholder = { Text(text = "Type your query here") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp), // Larger height for the explanation field
                maxLines = 5 // Limit the lines
            )
        }

        // Bottom content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Submit button
            Button(
                onClick = { /* Handle submit action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp), // Uniform button height
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004D40), // Dark teal color
                    contentColor = Color.White
                )
            ) {
                Text(text = "SUBMIT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Contact information
            Text(
                text = "You can contact us on this number 1234567892",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
    }
}