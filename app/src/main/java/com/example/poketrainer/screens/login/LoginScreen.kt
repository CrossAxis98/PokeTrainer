package com.example.poketrainer.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.poketrainer.components.*
import com.example.poketrainer.navigation.PokeTrainerScreens

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val isNewUser = rememberSaveable { mutableStateOf(false) }
    val areCredentialsWrote = rememberSaveable(email.value, password.value) {
        mutableStateOf(
            email.value.isNotEmpty() && password.value.isNotEmpty()
        )
    }

    ShowBars(isRequiredToShowBars = true)
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoText()
            InputField(
                textValue = email.value,
                onChange = { email.value = it },
                labelId = "Email"
            )
            PasswordInput(
                textValue = password.value,
                onChange = { password.value = it }
            )
            SubmitButton(
                isNewUser.value,
                areCredentialsWrote = areCredentialsWrote.value
            ) {
                val userEmail = email.value.trim()
                val userPassword = password.value.trim()

                if (isNewUser.value) {
                    viewModel.createNewUserAccount(userEmail, userPassword, context) {
                        navController.navigate(PokeTrainerScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                } else {
                    viewModel.signIn(userEmail, userPassword, context) {
                        navController.navigate(PokeTrainerScreens.HomeScreen.name) {
                            popUpTo(0)
                        }
                    }
                }
            }
            NewUserText(isNewUser.value) {
                isNewUser.value = !isNewUser.value
            }
        }
    }
}

