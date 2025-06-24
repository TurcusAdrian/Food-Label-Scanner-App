package com.example.food_label_scanner.ui_elements

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.food_label_scanner.LoginActivity
import com.example.food_label_scanner.R
import com.example.food_label_scanner.screens.Screens
import kotlinx.coroutines.launch


@Composable
fun DrawerContent(navController : NavHostController, drawerState: DrawerState) {

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    Text(
        text = "     Food Label Scanner", style = TextStyle(
            fontFamily = instrument_serif,
            fontSize = 32.sp
        )
    )
    HorizontalDivider()


    // Allergic Ingredients button:

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "Allergic Ingredients",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Allergic Ingredients",
                style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {
            scope.launch {
                navController.navigate(Screens.AllergicIngredients.screen)
                drawerState.close()
            }
        }
    )

    
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
        onClick = {scope.launch {
            navController.navigate(Screens.Account.screen)
            drawerState.close()
        } }
    )

    Spacer(Modifier.height(10.dp))

    // Barcode Scan button:

    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(R.drawable.barcode_icon),
                contentDescription = "Barcode Scan",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Barcode Scan", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {scope.launch {
            navController.navigate(Screens.BarcodeScanning.screen)
            drawerState.close()
        } }
    )

    Spacer(Modifier.height(10.dp))

    // Nutritional Dictionary button:

    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(R.drawable.nutritional_dictionary_icon),
                contentDescription = "Nutritional Dictionary",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Nutritional Dictionary", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {scope.launch {
            navController.navigate(Screens.NutritionalDictionary.screen)
            drawerState.close()
        }  }
    )

    Spacer(Modifier.height(10.dp))

    // How to use app button:

    NavigationDrawerItem(
        icon = {
            Icon(
                painter = painterResource(R.drawable.howtouse_icon),
                contentDescription = "How To Use",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "How to Use", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {
            scope.launch {
                navController.navigate(Screens.HowToUse.screen)
                drawerState.close()
            }
        }
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
        onClick = {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "https://github.com/TurcusAdrian/Food-Label-Scanner-App")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    )

    Spacer(Modifier.height(10.dp))

    //App settings button:

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Settings", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {scope.launch {
            navController.navigate(Screens.Settings.screen)
            drawerState.close()
        } }
    )

    Spacer(Modifier.height(10.dp))

    // About app button:

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "About",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "About", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {
            scope.launch {
                navController.navigate(Screens.About.screen)
                drawerState.close()
            }
        }
    )

    Spacer(Modifier.height(10.dp))

    // Log Out button:

    fun logout(context: Context) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("keepLoggedIn", false)
            remove("username")
            remove("user_password")
            apply()
        }

        // Navigate to the login screen
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear the back stack
        context.startActivity(intent)
    }

    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Log Out Icon",
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = "Log Out", style = TextStyle(
                    fontFamily = instrument_serif,
                    fontSize = 24.sp
                )
            )
        },
        selected = false,
        onClick = {
            scope.launch {
                logout(context)
                drawerState.close()
            }
        }
    )
}