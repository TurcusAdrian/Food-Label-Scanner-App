package com.example.food_label_scanner.ui_elements


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager

import com.example.food_label_scanner.data.*
import com.example.food_label_scanner.screens.*
import com.example.food_label_scanner.camera_functionality.*

import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.food_label_scanner.NotificationHelper
import com.example.food_label_scanner.screens.bottom_bar_screens.Favourites
import com.example.food_label_scanner.screens.drawer_screens.About
import com.example.food_label_scanner.screens.drawer_screens.Account
import com.example.food_label_scanner.screens.drawer_screens.Settings
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.Notifications
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.PrivacyPolicy
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.Support
import com.example.food_label_scanner.screens.drawer_screens.settings_screens.TermsOfService
import com.example.food_label_scanner.barcode_functionality.BarcodeDisplayScreen
import com.example.food_label_scanner.checkNotificationPermission
import com.example.food_label_scanner.database.IngredientDetailsViewModel
import com.example.food_label_scanner.screens.bottom_bar_screens.Home
import com.example.food_label_scanner.screens.bottom_bar_screens.Search
import com.example.food_label_scanner.screens.drawer_screens.*
import com.example.food_label_scanner.screens.drawer_screens.HowToUse
import com.example.food_label_scanner.text_recognition.TextRecognitionAnalyzer





@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Screen() {

    val context = LocalContext.current
    val viewModel: CameraViewModel = hiltViewModel()
    val navigationController = rememberNavController()

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

                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

                val textRecognitionAnalyzer = TextRecognitionAnalyzer { updatedText ->
                    onTextUpdated(updatedText)
                }
                textRecognitionAnalyzer.analyzeFromBitmap(bitmap)
            }
        }
    )


    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current


    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission) {
                ActivityCompat.requestPermissions(
                    context as androidx.activity.ComponentActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }
    }

    CameraPermission()

    val sharedTextViewModel: SharedTextViewModel = hiltViewModel()

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
                                        sharedTextViewModel.clearDetectedText()
                                        navigationController.navigate(Screens.Home.screen) {
                                            popUpTo(0)
                                        }
                                    }

                                    "Add from Gallery" -> {
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

                                    "Favourite Items" -> {
                                        navigationController.navigate(Screens.Favourites.screen) {
                                            popUpTo(0)
                                        }
                                    }
                                }
                            },
                            icon = {
                                Column(
                                    modifier = Modifier.padding(0.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (bottomNavItem.customIcon != null) {
                                        Icon(
                                            painter = painterResource(id = bottomNavItem.customIcon),
                                            contentDescription = bottomNavItem.title,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = if (index == selectedbutton) bottomNavItem.selectedIcon!! else bottomNavItem.unselectedIcon!!,
                                            contentDescription = bottomNavItem.title,
                                            modifier = Modifier.padding(1.dp)
                                        )
                                    }
                                }
                            },
                            label = {
                                Column(
                                    modifier = Modifier.padding(0.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = bottomNavItem.title)
                                }
                            }
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

                        Home(
                            selectedImage = selectedimage,
                            detectedText = detectedText,
                            onTextUpdated = { detectedText = it },
                            sharedTextViewModel = sharedTextViewModel,
                            navigationController = navigationController,
                            onClearSelectedImage = { selectedimage = null }
                        )

                    }


                    composable(Screens.Search.screen) { Search(navigationController) }
                    composable(Screens.Favourites.screen) { Favourites() }

                    //Drawer screens:
                    composable(Screens.About.screen) { About() }
                    composable(Screens.HowToUse.screen) { HowToUse() }
                    composable(Screens.Settings.screen) { Settings(navigationController) }
                    composable(Screens.BarcodeScanning.screen) { BarcodeScanning(navigationController) }
                    composable(Screens.NutritionalDictionary.screen) { NutritionalDictionary() }
                    composable(Screens.Account.screen) { Account() }


                    //Settings screens:
                    composable(Screens.Support.screen) { Support() }
                    composable(Screens.TermsOfService.screen) { TermsOfService(navigationController) }
                    composable(Screens.PrivacyPolicy.screen) { PrivacyPolicy(navigationController) }
                    composable(Screens.Notifications.screen) { Notifications() }

                    composable(
                        route = Screens.IngredientDetails.screen,
                        arguments = listOf(navArgument("ingredientId") {
                            type = NavType.IntType
                        })
                    ) { backStackEntry ->
                        val ingredientId =
                            backStackEntry.arguments?.getInt("ingredientId") ?: -1
                        IngredientDetails(ingredientId = ingredientId)
                    }

                    composable(Screens.BarcodeDisplay.screen) { backStackEntry ->
                        val barcode = backStackEntry.arguments?.getString("barcode")
                        if(barcode==null){
                            Text("Barcode doesn't exist")
                        }else {
                            barcode?.let {
                                BarcodeDisplayScreen(barcode = it)
                            }
                        }
                    }

                    composable(Screens.AllergicIngredients.screen) { AllergicIngredientsList() }

                    composable(
                        route = Screens.TextDisplay.routeWithoutArgs
                    ) {
                        val currentDetectedTextFromVM by sharedTextViewModel.detectedTextState.collectAsState()

                        Log.d("NavGraph", "TextDisplayScreen recomposing. Text from VM: $currentDetectedTextFromVM")

                        TextDisplayScreen(
                            detectedText = currentDetectedTextFromVM ?: "No text detected or available",
                            onBackClick = {
                                sharedTextViewModel.clearDetectedText()
                                navigationController.popBackStack(Screens.Home.screen, inclusive = false)
                            }
                        )
                    }

                }
            }
        }
    }
}
