package com.example.food_label_scanner.screens.drawer_screens

import android.content.ContentValues
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.food_label_scanner.DBHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.PasswordVisualTransformation


@Composable
fun Account() {
    val context = LocalContext.current
    val dbHelper = DBHelper(context)
    var currentUsername by remember { mutableStateOf("") }
    var showChangeUsernameDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch current user's username when the composable is first created
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            // For this example, let's assume the first user in the database is the current user
            val cursor = dbHelper.readableDatabase.query(
                "data",
                arrayOf("username"),
                null,
                null,
                null,
                null,
                null
            )
            cursor.use {
                if (it.moveToFirst()) {
                    val username = it.getString(it.getColumnIndexOrThrow("username"))
                    withContext(Dispatchers.Main) {
                        currentUsername = username
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Username: $currentUsername", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showChangeUsernameDialog = true }) {
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

@Composable
fun ChangeUsernameDialog(onDismiss: () -> Unit, onUsernameChanged: (String) -> Unit) {
    var newUsername by remember { mutableStateOf(TextFieldValue()) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Change Username", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newUsername,
                    onValueChange = { newUsername = it },
                    label = { Text("New Username") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onUsernameChanged(newUsername.text)
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

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Change Password", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onPasswordChanged(newPassword.text)
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