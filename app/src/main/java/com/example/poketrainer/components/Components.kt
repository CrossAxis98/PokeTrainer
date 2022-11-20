package com.example.poketrainer.components

import android.os.Build
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.utils.getColorByPokemonType

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

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    modifier: Modifier = Modifier,
    showGif: Boolean = false
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    val imageUrl: String = if(pokemon.sprites!!.front_default == null)
        "https://images.unsplash.com/photo-1541963463532-d68292c34b19?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=80&q=80"
    else {
        if (showGif) {
            pokemon.sprites!!.versions.generationV.blackWhite.animated.front_default
        } else {
            pokemon.sprites.front_default
        }
    }
    Card(
        modifier = modifier
            .size(
                width = 120.dp,
                height = 150.dp
            ),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        getColorByPokemonType(pokemon.types?.get(0)?.type!!.name),
                        Color.White
                    )
                )
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Pokemon Image",
                imageLoader = imageLoader,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = pokemon.name.toString().replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun PokemonCardInRow(
    pokemon: PokemonBasicInfo,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val imageUrl = pokemon.imageUrl
    val pokemonName = pokemon.name
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .clickable {
                navController.navigate("${PokeTrainerScreens.DetailScreen.name}/" +
                        pokemonName.replaceFirstChar { it.lowercase() } +
                        "/${pokemon.isMarkedAsWannaCatch}" +
                        "/${pokemon.isMarkedAsCaught}")
            },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.White
                    )
                )
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Pokemon Image",
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = pokemonName.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PokeTrainerFAB(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { onClick.invoke() },
        backgroundColor = Color.Gray
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search icon",
            tint = Color.White
        )
    }
}