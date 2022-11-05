package com.example.poketrainer.screens.details

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailsScreen(navController: NavController, pokemonName: String) {
    Text(text = "Hi, $pokemonName")
}