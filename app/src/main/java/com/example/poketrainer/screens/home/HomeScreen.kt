package com.example.poketrainer.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.poketrainer.utils.ShowBars

@Composable
fun HomeScreen(navController: NavController) {

    ShowBars(isRequiredToShowBars = true)
    Scaffold(
        topBar = { PokeTrainerAppBar(
            isHomeScreen = true,
            navController = navController
        ) },
        floatingActionButton = {}
    ) {

    }
}

@Composable
fun PokeTrainerAppBar(
    title: String = "PokeTrainer",
    isHomeScreen: Boolean = false,
    navController: NavController,
    icon: ImageVector? = null,
    onArrowBackClicked: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(horizontalArrangement = Arrangement.Start) {
                if (icon == null) {
                    Box {}
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Icon TopAppBar",
                        modifier = Modifier.clickable {
                            onArrowBackClicked.invoke()
                        }
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(
                    text = title,
                    color = Color.Red
                )

            }
        },
        actions = {
                  if (isHomeScreen) {
                      IconButton(onClick = { /*TODO*/ }) {
                          Icon(
                              imageVector = Icons.Default.Person,
                              contentDescription = "Person icon"
                          )
                      }
                  }
        } ,
        backgroundColor = Color.Transparent
    )
}
