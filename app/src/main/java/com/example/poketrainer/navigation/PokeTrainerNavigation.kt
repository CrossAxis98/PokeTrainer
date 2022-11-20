package com.example.poketrainer.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.poketrainer.screens.SplashScreen
import com.example.poketrainer.screens.details.DetailsScreen
import com.example.poketrainer.screens.home.HomeScreen
import com.example.poketrainer.screens.login.LoginScreen
import com.example.poketrainer.screens.search.SearchScreen

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
        composable(PokeTrainerScreens.SearchScreen.name) {
            SearchScreen(navController)
        }
        val detailsScreenRoute = "${PokeTrainerScreens.DetailScreen.name}/" +
                "{pokemonName}/{isMarkedAsWannaCatch}/{isCaught}"
        composable(
            detailsScreenRoute,
            arguments = listOf(
                navArgument("pokemonName") { type = NavType.StringType },
                navArgument("isMarkedAsWannaCatch") { type = NavType.BoolType},
                navArgument("isCaught") { type = NavType.BoolType}
            )
        ) { backStackEntry ->
            DetailsScreen(
                pokemonName = backStackEntry.arguments!!.getString("pokemonName").toString(),
                navController = navController,
                isMarkedAsWannaCatch = backStackEntry.arguments!!.getBoolean("isMarkedAsWannaCatch"),
                isCaught = backStackEntry.arguments!!.getBoolean("isCaught"),
                )
            Log.d("XXX", "Pokemon ${backStackEntry.arguments!!.getString("pokemonName").toString()} " +
                    "is marked ${backStackEntry.arguments!!.getString("isMarkedAsWannaCatch")}")
        }
    }
}