package com.example.poketrainer.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.poketrainer.screens.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun StatsScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    val name: String = FirebaseAuth.getInstance()
        .currentUser!!.email!!.split("@")[0]
        .replaceFirstChar { it.uppercase() }
    val pokemonsList by remember { homeViewModel.pokemonList }
    val pokemonsCaughtList = pokemonsList.filter { pokemonBasicInfo -> pokemonBasicInfo.isMarkedAsCaught  }
    val pokemonsToCatchList = pokemonsList.filter { pokemonBasicInfo ->
        pokemonBasicInfo.isMarkedAsWannaCatch && !pokemonBasicInfo.isMarkedAsCaught
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(start = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = name, style = MaterialTheme.typography.h5)
        val pokemonCaughtEndText = if ( pokemonsCaughtList.size == 1) "pokemon" else "pokemons"
        Text(text = "You have ${pokemonsCaughtList.size} $pokemonCaughtEndText.", style = MaterialTheme.typography.h6)
        val pokemonWannaCatchEndText = if ( pokemonsToCatchList.size == 1) "pokemon" else "pokemons"
        Text(text = "Wanna catch ${pokemonsToCatchList.size} $pokemonWannaCatchEndText.", style = MaterialTheme.typography.h6)

    }

}