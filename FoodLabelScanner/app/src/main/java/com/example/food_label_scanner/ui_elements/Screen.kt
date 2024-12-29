package com.example.food_label_scanner.ui_elements

import com.example.food_label_scanner.data.*
import com.example.food_label_scanner.ui_elements.*

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun Screen(modifier: Modifier = Modifier){

    val drawerState = rememberDrawerState( initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var selectedbutton by remember{
        mutableIntStateOf(0)
    }

    var selectedimage by remember {
        mutableStateOf<Uri?>(null)
    }

    var detectedText by remember { mutableStateOf("No text detected yet...") }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }

    //activity result launcher for picking image
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent()
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
                                        selectedimage = null
                                    }

                                }

                            },
                            icon = {
                                Icon(
                                    imageVector =
                                    if(index == selectedbutton)
                                        bottomNavItem.selectedIcon
                                    else
                                        bottomNavItem.unselectedIcon,
                                    contentDescription = bottomNavItem.title)
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
        ){ padding ->
            Box( modifier = Modifier.padding(padding)
                .fillMaxSize()
            ){
                if(selectedimage!=null){
                    DisplayImagePreview(selectedImage = selectedimage, onImageClick = {selectedimage =null})
                }
                else{
                    CameraContent(
                        detectedText = detectedText,
                        onDetectedTextUpdated = { detectedText = it }
                    )
                }
            }
        }
    }
}