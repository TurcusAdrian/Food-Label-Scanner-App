package com.example.food_label_scanner.ui_elements

import android.Manifest
import android.content.Context
import com.example.food_label_scanner.data.*
import com.example.food_label_scanner.screens.*
import com.example.food_label_scanner.camera_functionality.*

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.example.food_label_scanner.screens.bottom_bar_screens.Favourites
import com.example.food_label_scanner.screens.bottom_bar_screens.Search
import com.example.food_label_scanner.screens.drawer_screens.About
import com.example.food_label_scanner.screens.drawer_screens.Account
import com.example.food_label_scanner.screens.drawer_screens.Settings
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.CommunityGuidelines
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.Notifications
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.PrivacyPolicy
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.Support
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.TermsOfService

import com.example.food_label_scanner.NotificationHelper
import com.example.food_label_scanner.checkNotificationPermission
import com.example.food_label_scanner.screens.drawer_screens.Friends
//import com.example.food_label_scanner.screens.drawer_screens.Friends
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.BlockedAccounts
import com.example.food_label_scanner.text_recognition.TextRecognitionAnalyzer

fun retrieveUserId(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("userId", -1) // Default to -1 if not found
}

@Composable
fun Screen(modifier: Modifier = Modifier){

    val context = LocalContext.current
    val viewModel: CameraViewModel = hiltViewModel()
    val navigationController = rememberNavController()
    val selected = remember { mutableStateOf(Icons.Default.Home) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedbutton by remember { mutableIntStateOf(0) }
    var selectedimage by remember { mutableStateOf<Uri?>(null) }
    var detectedText by remember { mutableStateOf("No text detected yet...") }


    val notificationHelper = remember { NotificationHelper(context) }
    var hasNotificationPermission by remember { mutableStateOf(checkNotificationPermission(context)) }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    val photolauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedimage = uri
            uri?.let { imageUri ->
                // Convert URI to Bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                // Pass the bitmap to the TextRecognitionAnalyzer
                val textRecognitionAnalyzer = TextRecognitionAnalyzer { updatedText ->
                    onTextUpdated(updatedText) // Correctly pass the lambda function
                }
                textRecognitionAnalyzer.analyzeFromBitmap(bitmap)
            }
        }
    )


    /*
    val photolauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedimage = uri }
    )
    */
    AsyncImage(
        model = selectedimage,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current


    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission) {
                // Request permission if not granted
                ActivityCompat.requestPermissions(
                    context as androidx.activity.ComponentActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navigationController, drawerState = drawerState)
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    BottomNavItems.forEachIndexed { index, bottomNavItem ->
                        NavigationBarItem(
                            selected = index == selectedbutton,
                            onClick = {
                                selectedbutton = index
                                when (bottomNavItem.title) {
                                    "Home" -> {
                                        selectedimage = null
                                        detectedText = "No text detected yet..."
                                        navigationController.navigate(Screens.Home.screen) {
                                            popUpTo(0)
                                        }
                                    }
                                    "Add Photos from Gallery" -> {
                                        selectedimage = null
                                        photolauncher.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly
                                            )
                                        )
                                    }
                                    "Take Picture" -> {
                                        if (hasNotificationPermission) {
                                            notificationHelper.sendTakePictureNotification()
                                        }
                                        viewModel.photoCapture(lifecycleOwner) { bitmap ->
                                            viewModel.storePhotoInGallery(bitmap)
                                        }
                                    }
                                    "Search" -> {
                                        navigationController.navigate(Screens.Search.screen) {
                                            popUpTo(0)
                                        }
                                    }
                                    "Favourites Items" -> {
                                        navigationController.navigate(Screens.Favourites.screen) {
                                            popUpTo(0)
                                        }
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedbutton) bottomNavItem.selectedIcon else bottomNavItem.unselectedIcon,
                                    contentDescription = bottomNavItem.title
                                )
                            },
                            label = { Text(text = bottomNavItem.title) }
                        )
                    }
                }
            },
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        scope.launch { drawerState.apply { if (isClosed) open() else close() } }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController = navigationController,
                    startDestination = Screens.Home.screen
                ) {
                    composable(Screens.Home.screen) {
                        if (selectedimage != null) {
                            DisplayImagePreview(
                                selectedImage = selectedimage,
                                detectedText = detectedText, // Pass the detected text here
                                onImageClick = { selectedimage = null }
                            )
                        } else {
                            CameraContent(
                                detectedText = detectedText,
                                onDetectedTextUpdated = { detectedText = it },
                                onPhotoCaptured = { bitmap -> viewModel.storePhotoInGallery(bitmap) }
                            )
                        }
                    }
                    composable(Screens.Search.screen) { Search() }
                    composable(Screens.Favourites.screen) { Favourites() }
                    //Drawer screens:
                    composable(Screens.About.screen) { About() }
                    composable(Screens.Settings.screen) { Settings(navigationController) }
                    composable(Screens.Friends.screen) { Friends() }
                    //composable(Screens.SearchHistory.screen) {SearchHistory()}
                    composable(Screens.Account.screen) { Account() }
                    //Settings screens:
                    composable(Screens.Support.screen) { Support() }
                    composable(Screens.CommunityGuidelines.screen) { CommunityGuidelines() }
                    composable(Screens.TermsOfService.screen) { TermsOfService(navigationController) }
                    composable(Screens.PrivacyPolicy.screen) { PrivacyPolicy(navigationController) }
                    composable(Screens.Notifications.screen) { Notifications() }
                    composable(Screens.BlockedAccounts.screen) { BlockedAccounts() }
                }
            }
        }
    }
}
