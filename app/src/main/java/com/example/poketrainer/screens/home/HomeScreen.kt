package com.example.poketrainer.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poketrainer.components.PokeTrainerAppBar
import com.example.poketrainer.components.PokeTrainerFAB
import com.example.poketrainer.components.PokemonCardInRow
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.screens.search.PokemonSearchViewModel
import com.example.poketrainer.utils.ShowBars
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.tasks.await

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember  { viewModel.isLoading }
//    var pokemonList by remember {
//        mutableStateOf(emptyList<PokemonBasicInfo>())
//    }
//    if (viewModel.pokemonList.value.isNotEmpty()) {
//        pokemonList = viewModel.pokemonList.value
//    }

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

//                Log.d("XXX", "Renderowanie obrazu, size ${pokemonList.size}")
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


