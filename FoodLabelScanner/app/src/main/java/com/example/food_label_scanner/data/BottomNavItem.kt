package com.example.food_label_scanner.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

val BottomNavItems = listOf(
    BottomNavItem(
        title = "Home",
        route = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),

    BottomNavItem(
        title = "Add Photos from Gallery" ,
        route = "Add Photos from Gallery",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    ),

    BottomNavItem(
        title = "Take Picture",
        route = "Take Picture",
        selectedIcon = Icons.Filled.Call, //TO DO: change it to be camera
        unselectedIcon = Icons.Outlined.Call
    ),

    BottomNavItem(
        title = "Search",
        route = "Search",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),

    BottomNavItem(
        title = "Favourites Items",
        route = "Favourites Items",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star
    )
)


data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)