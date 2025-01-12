package com.example.food_label_scanner.screens.drawer_screens.settings_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.BlockedAccountsViewModel
import com.example.food_label_scanner.data.BlockedAccountItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockedAccounts(
    viewModel: BlockedAccountsViewModel = hiltViewModel() // Inject ViewModel
) {
    val blockedAccounts by viewModel.blockedAccounts.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var newBlockAccount by remember { mutableStateOf("") }

    // Replace with the ID of the logged-in user
    val currentUserId = 1 // Example: Get the current user ID dynamically
    LaunchedEffect(currentUserId) {
        viewModel.setCurrentUser(currentUserId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Blocked Accounts") })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Blocked Accounts",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (blockedAccounts.isEmpty()) {
                Text(text = "No accounts are blocked.")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(blockedAccounts) { blockedAccount ->
                        BlockedAccountItem(
                            blockedAccount = blockedAccount,
                            onUnblock = { viewModel.unblockUser(blockedAccount) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { showDialog = true }) {
                Text(text = "Block a User")
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Block User") },
                    text = {
                        OutlinedTextField(
                            value = newBlockAccount,
                            onValueChange = { newBlockAccount = it },
                            label = { Text("Enter username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.blockUser(newBlockAccount)
                            showDialog = false
                            newBlockAccount = ""
                        }) {
                            Text("Block")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

