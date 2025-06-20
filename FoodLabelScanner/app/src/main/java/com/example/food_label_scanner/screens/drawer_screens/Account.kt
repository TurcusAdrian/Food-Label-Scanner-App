package com.example.food_label_scanner.screens.drawer_screens

import android.content.ContentValues
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.food_label_scanner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.food_label_scanner.ui.theme.Cream
import com.example.food_label_scanner.ui.theme.Teal2


@Composable
fun Account() {
    val context = LocalContext.current
    val dbHelper = DBHelper(context)
    var currentUsername by remember { mutableStateOf("") }
    var showChangeUsernameDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val savedUsername = sharedPref.getString("username", null)

    LaunchedEffect(Unit) {
        if (savedUsername != null) {
            currentUsername = savedUsername
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Cream),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Account",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Username: $currentUsername",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                colors = ButtonColors(Teal2, Color.Black, Color.Black, Color.Black),
                onClick = { showChangeUsernameDialog = true }) {
                Text(text = "Change Username")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { showChangePasswordDialog = true }) {
                Text(text = "Change Password")
            }

            if (showChangeUsernameDialog) {
                ChangeUsernameDialog(
                    onDismiss = { showChangeUsernameDialog = false },
                    onUsernameChanged = { newUsername ->
                        coroutineScope.launch(Dispatchers.IO) {
                            updateUsername(dbHelper, currentUsername, newUsername)
                            withContext(Dispatchers.Main) {
                                currentUsername = newUsername
                                showChangeUsernameDialog = false
                            }
                        }
                    }
                )
            }

            if (showChangePasswordDialog) {
                ChangePasswordDialog(
                    onDismiss = { showChangePasswordDialog = false },
                    onPasswordChanged = { newPassword ->
                        coroutineScope.launch(Dispatchers.IO) {
                            updatePassword(dbHelper, currentUsername, newPassword)
                            withContext(Dispatchers.Main) {
                                showChangePasswordDialog = false
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ChangeUsernameDialog(onDismiss: () -> Unit, onUsernameChanged: (String) -> Unit) {

    var newUsername by remember { mutableStateOf(TextFieldValue()) }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Change Username", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it
                        showError = false},
                    label = { Text("New Username") },
                    isError = showError
                )
                if (showError) {
                    Text(
                        text = "New Username cannot be empty",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (newUsername.text.isNotBlank()) {
                            onUsernameChanged(newUsername.text)
                        }else{ showError = true}
                    }) {
                        Text("Change")
                    }
                }
            }
        }
    }
}

@Composable
fun ChangePasswordDialog(onDismiss: () -> Unit, onPasswordChanged: (String) -> Unit) {
    var newPassword by remember { mutableStateOf(TextFieldValue()) }
    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Change Password", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it
                        showError = false},
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError
                )
                if (showError) {
                    Text(
                        text = "New Password cannot be empty",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {if (newPassword.text.isNotBlank()) {
                        onPasswordChanged(newPassword.text)
                    }else{ showError = true}
                    }) {
                        Text("Change")
                    }
                }
            }
        }
    }
}

private suspend fun updateUsername(dbHelper: DBHelper, currentUsername: String, newUsername: String) {
    withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put("username", newUsername)
        }
        val db = dbHelper.writableDatabase
        val selection = "username = ?"
        val selectionArgs = arrayOf(currentUsername)
        db.update("data", values, selection, selectionArgs)
    }
}

private suspend fun updatePassword(dbHelper: DBHelper, currentUsername: String, newPassword: String) {
    withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put("password", newPassword)
        }
        val db = dbHelper.writableDatabase
        val selection = "username = ?"
        val selectionArgs = arrayOf(currentUsername)
        db.update("data", values, selection, selectionArgs)
    }
}