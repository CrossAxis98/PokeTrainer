package com.example.poketrainer.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.poketrainer.components.InputField

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: PokemonSearchViewModel = hiltViewModel()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val pokemonName = remember {
                mutableStateOf("")
            }
            InputField(
                textValue = pokemonName.value,
                onChange = { pokemonName.value = it },
                labelId = "Search",
                imeAction = ImeAction.Search
            )
        }
    }
}