package com.example.poketrainer.screens.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.poketrainer.components.InputField
import com.example.poketrainer.components.LogoText
import com.example.poketrainer.components.PasswordInput
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.utils.ShowBars
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    ShowBars(isRequiredToShowBars = true)

    val context = LocalContext.current

    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val isNewUser = rememberSaveable {
        mutableStateOf(false)
    }
    val areCredentialsWrote = rememberSaveable(email.value, password.value) {
        mutableStateOf(
            email.value.isNotEmpty() && password.value.isNotEmpty()
        )
    }

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
                        navController.navigate(PokeTrainerScreens.HomeScreen.name)
                    }
                } else {
                    viewModel.signIn(userEmail, userPassword, context) {
                        navController.navigate(PokeTrainerScreens.HomeScreen.name)
                    }
                }
            }
            NewUserText(isNewUser.value) {
                isNewUser.value = !isNewUser.value
            }
        }
    }
}

@Composable
fun NewUserText(isNewUser: Boolean, onClick: () -> Unit) {
    val newUserOrOldUserText = if (isNewUser) "Already have an account? " else "New user? "
    val createAnAccountOrLoginText = if (isNewUser) "Login" else "Create an account"
    Row {
        Text(text = newUserOrOldUserText)
        Text(
            createAnAccountOrLoginText,
            modifier = Modifier.clickable { onClick.invoke() },
            color = Color.Blue.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SubmitButton(
    isNewUser: Boolean,
    areCredentialsWrote: Boolean,
    onButtonClick: () -> Unit = {}
) {
    val buttonText = if (isNewUser) "Registry" else "Login"
    Button(
        onClick = { onButtonClick.invoke() },
        enabled = areCredentialsWrote
    ) {
        Text(text = buttonText)
    }
}
