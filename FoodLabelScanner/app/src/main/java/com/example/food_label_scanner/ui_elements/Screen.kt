package com.example.food_label_scanner.ui_elements

import com.example.food_label_scanner.data.*
import com.example.food_label_scanner.bottom_bar_screens.*
import com.example.food_label_scanner.camera_functionality.*

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner


@Composable
fun Screen(modifier: Modifier = Modifier){


    val viewModel: CameraViewModel = hiltViewModel()
    val navigationController = rememberNavController()
    val selected = remember { mutableStateOf(Icons.Default.Home) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedbutton by remember { mutableIntStateOf(0) }
    var selectedimage by remember { mutableStateOf<Uri?>(null) }
    var detectedText by remember { mutableStateOf("No text detected yet...") }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    val photolauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedimage = uri }
    )

    AsyncImage(
        model = selectedimage,
        contentDescription = null,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
    )

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(navigationController)
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
                    composable(Screens.About.screen) { About() }
                }
            }
        }
    }
}
