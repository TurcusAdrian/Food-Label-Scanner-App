package com.example.food_label_scanner.ui_elements

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.food_label_scanner.R

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
        title = { Text(text ="  Food Label Scanner", style = TextStyle(
            fontFamily = instrument_serif,
            fontSize = 32.sp)
        ) }
    )
}