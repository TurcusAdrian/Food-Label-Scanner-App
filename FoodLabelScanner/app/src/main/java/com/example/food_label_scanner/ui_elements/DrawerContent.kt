package com.example.food_label_scanner.ui_elements

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R

@Composable
fun DrawerContent(modifier: Modifier = Modifier) {

    val instrument_serif = FontFamily(Font(R.font.instrument_serif_regular))

    Text(
        text = "Food Label Scanner", style = TextStyle(
            fontFamily = instrument_serif,
            fontSize = 32.sp
        )
    )
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
        onClick = {/*TODO*/ }
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
        onClick = {/*TODO*/ }
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
        onClick = {/*TODO*/ }
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
        onClick = {/*TODO*/ }
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
        onClick = {/*TODO*/ }
    )

    Spacer(Modifier.height(10.dp))

    // About the app button:

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
        onClick = {/*TODO*/ }
    )


}