package com.example.poketrainer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.poketrainer.screens.SplashScreen
import com.example.poketrainer.screens.home.HomeScreen
import com.example.poketrainer.screens.login.LoginScreen

@Composable
fun PokeTrainerNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PokeTrainerScreens.SplashScreen.name
    ) {
        composable(PokeTrainerScreens.SplashScreen.name) {
            SplashScreen(navController)
        }
        composable(PokeTrainerScreens.LoginScreen.name) {
            LoginScreen(navController)
        }
        composable(PokeTrainerScreens.HomeScreen.name) {
            HomeScreen(navController)
        }

    }
}