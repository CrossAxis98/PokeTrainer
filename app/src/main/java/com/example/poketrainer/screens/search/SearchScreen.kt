package com.example.poketrainer.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val loadError by remember { viewModel.loadErrorAllPokemons }
    val pokemonName = remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Start,
//                verticalAlignment = CenterVertically
//            ) {
//                IconButton(onClick = { navController.popBackStack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Arrow back"
//                    )
//                }
                InputField(
                    textValue = pokemonName.value,
                    onChange = { pokemonName.value = it },
                    labelId = "Search",
                    imeAction = ImeAction.Search
                )
//            }

            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn() {
                items(pokemonList.size/2) { index ->
                    if (index >= pokemonList.size/2 - 2 && !endReached && !isLoading) {
                        viewModel.getAllPokemons()
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = CenterVertically
                    ) {
                        PokemonCardInRow(
                            pokemon = pokemonList[index * 2],
                            modifier = Modifier
                                .padding(12.dp)
                                .weight(1f),
                            navController = navController
                        )
                        PokemonCardInRow(
                            pokemon = pokemonList[index * 2 + 1],
                            modifier = Modifier
                                .padding(12.dp)
                                .weight(1f),
                            navController = navController
                        )
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

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Center
            ) {
                if (loadError.isNotEmpty()) {
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