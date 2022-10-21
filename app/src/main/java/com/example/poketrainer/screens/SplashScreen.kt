package com.example.poketrainer.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.utils.ShowBars
import kotlinx.coroutines.delay
import com.example.poketrainer.components.PokeTrainerLogo
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

