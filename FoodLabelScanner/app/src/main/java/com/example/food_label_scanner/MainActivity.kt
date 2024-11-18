package com.example.food_label_scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.ui.theme.FoodLabelScannerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodLabelScannerTheme {
                Screen()
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier){

    val drawerState = rememberDrawerState( initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()

    var selectedbutton by remember{
        mutableIntStateOf(0)
    }

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
                            label = {Text(text = bottomNavItem.title)}
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
            ScreenContent(modifier = Modifier.padding(padding))
        }
    }
}


@Composable
fun DrawerContent(modifier: Modifier = Modifier){

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))

    Text(text = "Food Label Scanner", style = TextStyle(
        fontFamily = instrument_serif,
        fontSize = 32.sp))
    HorizontalDivider()

    // Account button:

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "Account",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Account", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {/*TODO*/}
    )

    Spacer(Modifier.height(10.dp))

    // Search History icon

    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(R.drawable.history_icon),
                contentDescription = "Search History",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Search History", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {/*TODO*/}
    )

    Spacer(Modifier.height(10.dp))

    // Friends button:

    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(R.drawable.friends_icon),
                contentDescription = "Friends",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Friends", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {/*TODO*/}
    )

    Spacer(Modifier.height(10.dp))

    // Share app button:

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Share", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {/*TODO*/}
    )

    Spacer(Modifier.height(10.dp))

    //App settings button:

    NavigationDrawerItem(
        icon ={
            Icon( imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(text = "Settings", style = TextStyle(
                fontFamily = instrument_serif,
                fontSize = 24.sp))
        },
        selected = false,
        onClick = {/*TODO*/}
    )

    Spacer(Modifier.height(10.dp))

    // About the app button:

    NavigationDrawerItem(
        icon ={
            Icon( imageVector = Icons.Filled.Info,
                contentDescription = "About",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(text = "About", style = TextStyle(
                fontFamily = instrument_serif,
                fontSize = 24.sp))
        },
        selected = false,
        onClick = {/*TODO*/}
    )



}

@Composable
fun ScreenContent(modifier: Modifier = Modifier){
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onOpenDrawer: () -> Unit
){
    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))

    TopAppBar(navigationIcon = {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier.clickable { onOpenDrawer() }
        ) },
        title = { Text(text ="Screen Name", style = TextStyle(
            fontFamily = instrument_serif,
            fontSize = 32.sp)
        ) }
    )
}

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

