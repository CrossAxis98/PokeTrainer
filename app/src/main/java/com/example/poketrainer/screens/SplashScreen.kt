package com.example.poketrainer.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.poketrainer.navigation.PokeTrainerScreens
import kotlinx.coroutines.delay
import com.example.poketrainer.components.PokeTrainerLogo
import com.example.poketrainer.components.ShowBars
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }
    ShowBars(isRequiredToShowBars = false)

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.65f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            )
        )
        delay(2000L)
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(PokeTrainerScreens.LoginScreen.name) {
                popUpTo(0)
            }
        } else {
            navController.navigate(PokeTrainerScreens.HomeScreen.name) {
                popUpTo(0)
            }
        }
    }
    PokeTrainerLogo(scale)
}

