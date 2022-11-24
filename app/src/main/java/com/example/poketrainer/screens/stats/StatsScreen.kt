package com.example.poketrainer.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun StatsScreen(navController: NavController) {
    val name: String = FirebaseAuth.getInstance()
        .currentUser!!.email!!.split("@")[0]
        .replaceFirstChar { it.uppercase() }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = name)
    }

}