package com.example.poketrainer.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poketrainer.components.PokeTrainerAppBar
import com.example.poketrainer.components.PokeTrainerFAB
import com.example.poketrainer.components.PokemonCardInRow
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.utils.ShowBars

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember  { viewModel.isLoading }

    ShowBars(isRequiredToShowBars = true)

    Scaffold(
        modifier = Modifier.padding(10.dp),
        topBar = {
            PokeTrainerAppBar(
            isHomeScreen = true,
            navController = navController
        ) },
        floatingActionButton = {
            PokeTrainerFAB() {
                navController.navigate(PokeTrainerScreens.SearchScreen.name)
            }
        }
    ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Text(
                    text = "Pokemons you want to catch:",
                    style = MaterialTheme.typography.h5
                )
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    LazyRow {
                        items(pokemonList.size) { index ->
                            PokemonCardInRow(pokemon = pokemonList[index], navController = navController)
                        }
                    }
                }
                Text(
                    text = "Pokemons you've already caught:",
                    style = MaterialTheme.typography.h5
                )
            }
    }
}


