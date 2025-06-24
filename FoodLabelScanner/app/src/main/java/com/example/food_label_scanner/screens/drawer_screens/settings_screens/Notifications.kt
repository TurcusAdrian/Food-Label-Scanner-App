package com.example.food_label_scanner.screens.drawer_screens.settings_screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import android.content.Context
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.food_label_scanner.ui.theme.Cream

@Composable
fun Notifications() {
    val context = LocalContext.current
    var notificationSettings by remember {
        mutableStateOf(getNotificationSettings(context))
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Cream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Notification Settings",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(notificationSettings.keys.toList()) { notificationType ->
                    NotificationItem(
                        notificationType = notificationType,
                        isChecked = notificationSettings[notificationType] ?: false,
                        onCheckedChange = { isChecked ->
                            notificationSettings = notificationSettings.toMutableMap().apply {
                                this[notificationType] = isChecked
                            }
                            saveNotificationSettings(context, notificationSettings)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notificationType: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = notificationType, modifier = Modifier.weight(1f), color = Color.Black)
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

private fun getNotificationSettings(context: Context): Map<String, Boolean> {
    val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
    return mapOf(
        "Take Picture Notifications" to sharedPreferences.getBoolean("take_picture_notifications", true),
    )
}

private fun saveNotificationSettings(context: Context, settings: Map<String, Boolean>) {
    val sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    settings.forEach { (notificationType, isEnabled) ->
        when (notificationType) {
            "Take Picture Notifications" -> editor.putBoolean("take_picture_notifications", isEnabled)
        }
    }
    editor.apply()
}