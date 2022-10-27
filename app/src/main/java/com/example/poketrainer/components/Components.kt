package com.example.poketrainer.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PokeTrainerLogo(scale: Animatable<Float, AnimationVector1D>) {
    Surface(
        modifier = Modifier
            .size(300.dp)
            .scale(scale.value),
        shape = CircleShape,
        elevation = 4.dp,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Red,
                            Color.White
                        ),
                        startY = 0f,
                        endY = 350f
                    )
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LogoText()
        }
    }
}

@Composable
fun LogoText() {
    val configuration = LocalConfiguration.current
    Text(
        text = "PokeTrainer",
        color = Color.Black.copy(alpha = 0.7f),
        style = if (configuration.screenWidthDp <= 360) MaterialTheme.typography.h4
                else MaterialTheme.typography.h3,
        fontWeight = FontWeight.SemiBold
    )
    Text(
        text = "Catch'Em all!",
        color = Color.Gray,
        style = MaterialTheme.typography.h5
    )
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
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.SemiBold
                )

            }
        },
        //modifier = Modifier.,
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
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    textValue: String,
    onChange: (String) -> Unit,
    labelId: String,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = { onChange.invoke(it) },
        label = { Text(text = labelId)},
        enabled = enabled,
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    textValue: String,
    onChange: (String) -> Unit,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isPasswordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val visualTransformation = if (isPasswordVisible.value) VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = textValue,
        onValueChange = { onChange.invoke(it) },
        label = { Text(text = "Password")},
        enabled = enabled,
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
        },
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (isPasswordVisible.value) {
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(imageVector = Icons.Filled.Visibility, contentDescription = "Hide Password" )
                }
            } else {
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(imageVector = Icons.Filled.VisibilityOff, contentDescription = "Show Password" )
                }
            }
        }
    )
}