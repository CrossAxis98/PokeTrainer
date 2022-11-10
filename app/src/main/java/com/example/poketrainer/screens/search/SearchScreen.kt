package com.example.poketrainer.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poketrainer.components.InputField
import com.example.poketrainer.components.PokemonCardInRow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: PokemonSearchViewModel = hiltViewModel()
) {

    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    val pokemonName = remember { mutableStateOf("") }
    val isSearching by remember { viewModel.isSearching }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSearchRequested by remember { viewModel.isSearchRequested }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally
        ) {
                InputField(
                    textValue = pokemonName.value,
                    onChange = {
                        pokemonName.value = it
                        viewModel.searchPokemonFromAlreadyLoadedList(it)
                               },
                    labelId = "Search",
                    imeAction = ImeAction.Search,
                    onAction = KeyboardActions {
                        viewModel.searchSpecifiedPokemon(pokemonName.value.trim())
                        isSearchRequested = true
                        keyboardController?.hide()
                    }
                )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn() {
                items(pokemonList.size/2 + 1) { index ->
                    if (index >= pokemonList.size/2 - 2 && !endReached && !isLoading && !isSearching) {
                        viewModel.getAllPokemons()
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = CenterVertically
                    ) {
                        if (index * 2 < pokemonList.size) {
                            PokemonCardInRow(
                                pokemon = pokemonList[index * 2],
                                modifier = Modifier
                                    .padding(12.dp)
                                    .weight(1f),
                                navController = navController
                            )
                        }
                        if (index * 2 + 1 < pokemonList.size) {
                            PokemonCardInRow(
                                pokemon = pokemonList[index * 2 + 1],
                                modifier = Modifier
                                    .padding(12.dp)
                                    .weight(1f),
                                navController = navController
                            )
                        }
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
            if (!isLoading && pokemonList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    if (isSearchRequested) {
                        Text(
                            text = "We didn't find matching pokemon.",
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "We didn't find matching pokemon in current loaded set.\n" +
                                    "Enter whole name and press search to check whole database.",
                            textAlign = TextAlign.Center
                        )

                    }
                }
            }
            if (loadError.isNotEmpty() && !loadError.contains("404", ignoreCase = true)) {
                Box(
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(),
                contentAlignment = Center
                ) {
                    RetrySection(error = loadError) {
                        viewModel.getAllPokemons()
                    }
                }
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = error,
            color = Color.Red,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry.invoke() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}