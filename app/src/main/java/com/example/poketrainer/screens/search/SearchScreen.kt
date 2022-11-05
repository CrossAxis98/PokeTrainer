package com.example.poketrainer.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.poketrainer.components.InputField
import com.example.poketrainer.components.PokemonCardInRow

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: PokemonSearchViewModel = hiltViewModel()
) {

    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val isLoading by remember { viewModel.isLoadingAllPokemons }
    val pokemonName = remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                textValue = pokemonName.value,
                onChange = { pokemonName.value = it },
                labelId = "Search",
                imeAction = ImeAction.Search
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn() {
                items(pokemonList.size/2) { index ->
                    if (index >= pokemonList.size/2 - 2 && !endReached && !isLoading) {
                        viewModel.getAllPokemons()
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PokemonCardInRow(pokemonList[index * 2], modifier = Modifier
                            .padding(10.dp)
                            .weight(1f))
                        Spacer(modifier = Modifier.width(20.dp))
                        PokemonCardInRow(pokemonList[index * 2 + 1], modifier = Modifier
                            .padding(10.dp)
                            .weight(1f))
                    }
                }
                item {
                    if (isLoading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}