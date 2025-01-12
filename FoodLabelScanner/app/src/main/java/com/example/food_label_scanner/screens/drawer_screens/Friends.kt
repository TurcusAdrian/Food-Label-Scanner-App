package com.example.food_label_scanner.screens.drawer_screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.food_label_scanner.FriendsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Friends(viewModel: FriendsViewModel = hiltViewModel()) {
    var usernameToAdd by remember { mutableStateOf("") }
    var showAddFriendDialog by remember { mutableStateOf(false) }

    val friendRequests by viewModel.friendRequests.collectAsState(initial = emptyList())
    val friendsList by viewModel.friendsList.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(title = { Text("Friends") })

        // Add Friends Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { showAddFriendDialog = true }) {
                Text("Add Friend")
            }

            if (showAddFriendDialog) {
                AlertDialog(
                    onDismissRequest = { showAddFriendDialog = false },
                    title = { Text("Send Friend Request") },
                    text = {
                        OutlinedTextField(
                            value = usernameToAdd,
                            onValueChange = { usernameToAdd = it },
                            label = { Text("Enter username") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            viewModel.sendFriendRequest(usernameToAdd)
                            usernameToAdd = ""
                            showAddFriendDialog = false
                        }) {
                            Text("Send")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showAddFriendDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Friend Requests Section
        Text(
            text = "Friend Requests",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        if (friendRequests.isEmpty()) {
            Text(
                text = "No incoming friend requests.",
                modifier = Modifier.padding(start = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(friendRequests) { request ->
                    FriendRequestItem(
                        username = request.username,
                        onAccept = { viewModel.acceptFriendRequest(request.username) },
                        onReject = { viewModel.rejectFriendRequest(request.username) }
                    )
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Friends List Section
        Text(
            text = "My Friends",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        if (friendsList.isEmpty()) {
            Text(
                text = "You have no friends yet.",
                modifier = Modifier.padding(start = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(friendsList) { friend ->
                    FriendItem(username = friend)
                }
            }
        }
    }
}

@Composable
fun FriendRequestItem(username: String, onAccept: () -> Unit, onReject: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = username, fontSize = 16.sp)
        Row {
            Button(onClick = onAccept) { Text("Accept") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onReject) { Text("Reject") }
        }
    }
}

@Composable
fun FriendItem(username: String) {
    Text(
        text = username,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}