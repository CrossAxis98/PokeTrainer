package com.example.poketrainer.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poketrainer.components.PokeTrainerAppBar
import com.example.poketrainer.components.PokeTrainerFAB
import com.example.poketrainer.components.PokemonCardInRow
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.screens.search.PokemonSearchViewModel
import com.example.poketrainer.utils.ShowBars

@Composable
fun HomeScreen(
    navController: NavController
//    viewModel: PokemonSearchViewModel = hiltViewModel()
) {
//    val pokemonList by remember { viewModel.pokemonList }
//    val endReached by remember { viewModel.endReached }
//    val isLoading by remember { viewModel.isLoadingAllPokemons }

    ShowBars(isRequiredToShowBars = true)

    Scaffold(
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
                Text(
                    text = "Pokemons you've already caught:",
                    style = MaterialTheme.typography.h5
                )
//                LazyColumn() {
//                    items(pokemonList.size/2) { index ->
//                        if (index >= pokemonList.size/2 - 2 && !endReached && !isLoading) {
//                                viewModel.getAllPokemons()
//                        }
//                        Row(modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            PokemonCardInRow(pokemonList[index * 2], modifier = Modifier.padding(10.dp).weight(1f))
//                            Spacer(modifier = Modifier.width(20.dp))
//                            PokemonCardInRow(pokemonList[index * 2 + 1], modifier = Modifier.padding(10.dp).weight(1f))
//                        }
//                        Spacer(modifier = Modifier.height(20.dp))
//                    }
//                    item {
//                        if (isLoading) {
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(8.dp),
//                                horizontalArrangement = Arrangement.Center
//                            ) {
//                                CircularProgressIndicator()
//                            }
//                        }
//                    }
//                }
            }
    }
}


