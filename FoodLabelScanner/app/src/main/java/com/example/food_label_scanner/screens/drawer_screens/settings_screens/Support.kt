package com.example.food_label_scanner.screens.drawer_screens.settings_screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import com.example.food_label_scanner.ui.theme.Cream

@Composable
fun Support() {
    // State for form fields
    val title = remember { mutableStateOf("") }
    val problem = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }

    // State for validation
    val isTitleValid = remember { mutableStateOf(true) }
    val isProblemValid = remember { mutableStateOf(true) }
    val isEmailValid = remember { mutableStateOf(true) }

    // Context for email intent
    val context = LocalContext.current
    val scrollState = rememberScrollState()


    // Email regex pattern
    val emailRegex = remember {
        Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream) // Light background color
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
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

            // Email input field with validation
            OutlinedTextField(
                value = userEmail.value,
                onValueChange = {
                    userEmail.value = it
                    isEmailValid.value = it.isBlank() || emailRegex.matches(it)
                },
                label = { Text(text = "Your Email") },
                placeholder = { Text(text = "Enter your email address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = !isEmailValid.value && userEmail.value.isNotBlank(),
                supportingText = {
                    if (!isEmailValid.value && userEmail.value.isNotBlank()) {
                        Text(
                            text = "Please enter a valid email address",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // Title input field
            OutlinedTextField(
                value = title.value,
                onValueChange = {
                    title.value = it
                    isTitleValid.value = it.isNotBlank()
                },
                label = { Text(text = "Title") },
                placeholder = { Text(text = "Add your grievance title here") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                isError = !isTitleValid.value,
                supportingText = {
                    if (!isTitleValid.value) {
                        Text(
                            text = "Title is required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // Problem input field
            OutlinedTextField(
                value = problem.value,
                onValueChange = {
                    problem.value = it
                    isProblemValid.value = it.isNotBlank()
                },
                label = { Text(text = "Explain the problem") },
                placeholder = { Text(text = "Type your query here") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5,
                isError = !isProblemValid.value,
                supportingText = {
                    if (!isProblemValid.value) {
                        Text(
                            text = "Problem description is required",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }

        // Bottom content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Submit button
            Button(
                onClick = {
                    // Validate inputs
                    isTitleValid.value = title.value.isNotBlank()
                    isProblemValid.value = problem.value.isNotBlank()

                    if (isTitleValid.value && isProblemValid.value) {
                        sendEmail(
                            context = context,
                            userEmail = userEmail.value,
                            subject = title.value,
                            message = problem.value
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF004D40), // Dark teal color
                    contentColor = Color.White
                ),
                enabled = title.value.isNotBlank() && problem.value.isNotBlank()
            ) {
                Text(text = "SUBMIT", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Contact information
            Text(
                text = "Or you can contact us on this number 0710 100 001",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }
    }
}

// Function to send email
private fun sendEmail(context: Context, userEmail: String, subject: String, message: String) {
    try {
        Log.i("Send email", "")

        // Define recipients
        val TO = arrayOf("adrian.turcus@student.upt.ro") // Change to your support email
        val CC = if (userEmail.isNotBlank()) arrayOf(userEmail) else emptyArray()

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            data = Uri.parse("mailto:")
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, TO)
            putExtra(Intent.EXTRA_CC, CC)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            // Create chooser to let user select email app
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."))
            Log.i("Finished sending email...", "")
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "There is no email client installed.",
                Toast.LENGTH_SHORT
            ).show()

            // Offer alternative contact methods
            System.out.println("error")
        }
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Error sending email: ${e.message}",
            Toast.LENGTH_LONG
        ).show()
    }
}