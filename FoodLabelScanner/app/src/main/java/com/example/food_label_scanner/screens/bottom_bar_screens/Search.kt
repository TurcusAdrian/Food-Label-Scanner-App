package com.example.food_label_scanner.screens.bottom_bar_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.food_label_scanner.screens.*

import com.example.food_label_scanner.search_bar_functionality.*

@Composable
fun Search(navController: NavHostController){
    val viewModel: SearchViewModel = hiltViewModel()
    val searchText by viewModel.searchText.collectAsState()
    val ingredients by viewModel.ingredients.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    val searchHistory by viewModel.searchHistory.collectAsState()


    val context = LocalContext.current




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Search") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            if (searchText.isBlank()) {
                // Show search history when search text is blank
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(searchHistory) { ingredient ->
                        TextButton(onClick = {navController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }}) {
                            Text(
                                text = ingredient.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            )
                        }
                    }
                }
            } else {
                // Show matching ingredients when search text is not blank
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(ingredients) { ingredient ->

                        TextButton(onClick = {navController.navigate(Screens.Home.screen) {
                            popUpTo(0)
                        }}) {
                            Text(
                                text = ingredient.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}