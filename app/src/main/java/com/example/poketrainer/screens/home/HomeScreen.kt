package com.example.poketrainer.screens.home

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.poketrainer.components.PokeTrainerAppBar
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.screens.search.PokemonSearchViewModel
import com.example.poketrainer.utils.ShowBars
import com.example.poketrainer.utils.getColorByPokemonType

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: PokemonSearchViewModel = hiltViewModel()
) {
    val pokemon = viewModel.pokemon
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    ShowBars(isRequiredToShowBars = true)
    Scaffold(
        topBar = { PokeTrainerAppBar(
            isHomeScreen = true,
            navController = navController
        ) },
        floatingActionButton = {}
    ) {
        if (viewModel.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Loading...")
                LinearProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) ,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Pokemons you want to catch:",
                    style = MaterialTheme.typography.h5
                )
                PokemonCard(pokemon!!, showGif = true)
                Text(
                    text = "Pokemons you've already caught:",
                    style = MaterialTheme.typography.h5
                )
                PokemonCard(pokemon!!, showGif = true)
            }
        }
    }
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
            if (SDK_INT >= 28) {
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

