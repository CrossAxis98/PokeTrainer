package com.example.poketrainer.components

import android.os.Build
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
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
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.abs
import kotlin.math.roundToInt



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
    isProfileMenuExpanded: Boolean = false,
    onProfileMenuClick: () -> Unit = {},
    onProfileMenuDismissRequest: () -> Unit = {},
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
                IconButton(onClick = onProfileMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person icon"
                    )
                }
                DropdownMenu(
                    expanded = isProfileMenuExpanded,
                    onDismissRequest = onProfileMenuDismissRequest
                ) {
                    DropdownMenuItem(onClick = {
                        navController.navigate(PokeTrainerScreens.StatsScreen.name)
                    }) {
                        Text(text = "Profile")
                    }
                    DropdownMenuItem(onClick = {
                        FirebaseAuth.getInstance().signOut().run {
                            navController.navigate(PokeTrainerScreens.LoginScreen.name)
                        }
                    }) {
                        Text(text = "Log out")
                    }
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
fun PokemonCardInRowEnableToDelete(
    pokemon: PokemonBasicInfo,
    modifier: Modifier = Modifier,
    navController: NavController,
    onRemove: () -> Unit = {}
) {
    val imageUrl = pokemon.imageUrl
    val pokemonName = pokemon.name
    val localDensity = LocalDensity.current
    var offsetY by remember { mutableStateOf(0f) }
    var cardHeightDp by remember { mutableStateOf(0.dp) }
    var cardWidthDp by remember { mutableStateOf(0.dp) }
    var deleteIconColor by remember { mutableStateOf(Color.Black) }

    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .onGloballyPositioned { coordinates ->
                cardHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                cardWidthDp = with(localDensity) { coordinates.size.width.toDp() }
            },
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Card(
        modifier = Modifier
            .clickable {
                navController.navigate("${PokeTrainerScreens.DetailScreen.name}/" +
                        pokemonName.replaceFirstChar { it.lowercase() } +
                        "/${pokemon.isMarkedAsWannaCatch}" +
                        "/${pokemon.isMarkedAsCaught}" +
                        "/${pokemon.dateOfCatch}")
            }
            .offset { IntOffset(0, offsetY.roundToInt()) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    if (offsetY <= 0) offsetY += delta
                },
                onDragStopped = { _ ->
                    Log.d("XXX", "Calling on drag stopped")
                    if (abs(offsetY) > 200) {
                        onRemove()
                    } else if (offsetY != 0f) {
                        offsetY = 0f
                    }
                }
            )
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
                if (abs(offsetY) > 200) {
                    deleteIconColor = Color.Red
                } else {
                    deleteIconColor = Color.Black
                }
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
        if (!offsetY.equals(0f)) {
            Box(
                modifier = Modifier
                    .height(cardHeightDp)
                    .width(cardWidthDp)
                    .padding(bottom = 4.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete icon",
                    tint = deleteIconColor
                )
            }
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        navController.navigate("${PokeTrainerScreens.DetailScreen.name}/" +
                                pokemonName.replaceFirstChar { it.lowercase() } +
                                "/${pokemon.isMarkedAsWannaCatch}" +
                                "/${pokemon.isMarkedAsCaught}" +
                                "/${pokemon.dateOfCatch}")
                    },
                    onLongPress = {

                    }
                )
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

@Composable
fun ButtonWithColorAsType(
    colorOfTheType: Color,
    buttonText: String,
    onClickAction: () -> Unit
) {

    TextButton(
        modifier = Modifier.padding(top = 10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White),
        shape = CircleShape,
        border = BorderStroke(3.dp, colorOfTheType),
        onClick = {
            onClickAction.invoke()
        },
        contentPadding = PaddingValues(10.dp)
    ) {
        Text(text = buttonText)
    }
}