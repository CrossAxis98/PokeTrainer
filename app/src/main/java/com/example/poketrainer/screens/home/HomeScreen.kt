package com.example.poketrainer.screens.home

import android.util.Log
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
import com.example.poketrainer.components.PokemonCardInRowEnableToDelete
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.utils.ShowBars

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val pokemonsList: MutableList<PokemonBasicInfo> = remember { viewModel.pokemonList }
    val isLoading by remember { viewModel.isLoading }
    var pokemonsToCatchList by remember { mutableStateOf(emptyList<PokemonBasicInfo>()) }
    var pokemonsCaughtList by remember { mutableStateOf(emptyList<PokemonBasicInfo>()) }
    var isProfileMenuExpanded by remember { mutableStateOf(false) }

    ShowBars(isRequiredToShowBars = true)
    Scaffold(
        modifier = Modifier.padding(10.dp),
        topBar = {
            PokeTrainerAppBar(
                isHomeScreen = true,
                navController = navController,
                isProfileMenuExpanded = isProfileMenuExpanded,
                onProfileMenuClick = { isProfileMenuExpanded = !isProfileMenuExpanded},
                onProfileMenuDismissRequest = { isProfileMenuExpanded = false }
            )
        },
        floatingActionButton = {
            PokeTrainerFAB() {
                navController.navigate(PokeTrainerScreens.SearchScreen.name)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Pokemons you've already caught:",
                style = MaterialTheme.typography.h5
            )
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                pokemonsCaughtList = pokemonsList.filter { pokemon ->
                    pokemon.isMarkedAsCaught
                }
                if (pokemonsCaughtList.isEmpty()) {
                    Text(
                        text = "No pokemons yet",
                    )
                } else {
                    LazyRow {
                        items(pokemonsCaughtList.size, key = { index ->
                            pokemonsCaughtList[index].number
                        }) { index ->
                            PokemonCardInRowEnableToDelete(
                                pokemon = pokemonsCaughtList[index],
                                navController = navController
                            ) {
                                viewModel.removePokemonFromFirestore(
                                    pokemonsCaughtList[index]
                                )
                            }
                        }
                    }
                }
            }
            Text(
                text = "Pokemons you want to catch:",
                style = MaterialTheme.typography.h5
            )
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                pokemonsToCatchList = pokemonsList.filter { pokemon ->
                    pokemon.isMarkedAsWannaCatch && !pokemon.isMarkedAsCaught
                }
                if (pokemonsToCatchList.isEmpty()) {
                    Text(
                        text = "No pokemons yet",
                    )
                } else {
                    LazyRow {
                        items(pokemonsToCatchList.size) { index ->
                            PokemonCardInRow(
                                pokemon = pokemonsToCatchList[index],
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

