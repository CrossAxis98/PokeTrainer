package com.example.poketrainer.screens.details

import android.os.Build
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.model.Type
import com.example.poketrainer.utils.getColorByPokemonType
import com.example.poketrainer.utils.parseTypeToColor
import com.example.poketrainer.R
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.example.poketrainer.navigation.PokeTrainerScreens
import com.example.poketrainer.utils.parseStatToAbbr
import com.example.poketrainer.utils.parseStatToColor
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun DetailsScreen(
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 50.dp,
    pokemonImageSize: Dp = 100.dp,
    viewModel: DetailsViewModel = hiltViewModel()
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
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
     value = viewModel.getPokemonDetails(pokemonName)
    }.value
//    val type = pokemonInfo.data!!.types!!.first().type.name
    PokemonDetailsMainScreen(
        pokemonInfo,
        navController,
        topPadding,
        pokemonImageSize,
        imageLoader
    )
}

@Composable
private fun PokemonDetailsMainScreen(
    pokemonInfo: Resource<Pokemon>,
    navController: NavController,
    topPadding: Dp,
    pokemonImageSize: Dp,
    imageLoader: ImageLoader
) {
    when (pokemonInfo) {
        is Resource.Success -> {
            val type = pokemonInfo.data!!.types?.get(0)?.type!!.name
            val colorByType = getColorByPokemonType(type)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorByType)
                    .padding(bottom = 16.dp)
            ) {
                PokemonDetailTopSection(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .align(Alignment.TopCenter)
                )
                PokemonDetailSection(
                    pokemonInfo = pokemonInfo.data,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = topPadding + pokemonImageSize / 1.3f,
                            start = 26.dp,
                            end = 26.dp,
                            bottom = 26.dp
                        )
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colors.surface)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    colorOfTheType = colorByType,
                    navController = navController
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    pokemonInfo.data.sprites?.let {
                        AsyncImage(
                            model = it.versions.generationV.blackWhite.animated.front_default,
                            contentDescription = "Pokemon image",
                            imageLoader = imageLoader,
                            modifier = Modifier
                                .size(pokemonImageSize)
                                .offset(y = topPadding)
                        )
                    }
                }
            }
        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
            )
        }
    }
}

@Composable
fun PokemonSaveSection(
    colorOfTheType: Color,
    currentPokemon: PokemonBasicInfo,
    navController: NavController
) {
    TextButton(
        modifier = Modifier.padding(top = 10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black, contentColor = Color.White),
        shape = CircleShape,
        border = BorderStroke(3.dp, colorOfTheType),
        onClick = { saveToFirebase(currentPokemon, navController) },
        contentPadding = PaddingValues(10.dp)
    ) {
            Text(text = "Wanna catch!")
    }
}

fun saveToFirebase(currentPokemon: PokemonBasicInfo, navController: NavController) {
    val db = Firebase.firestore
    db.collection("pokemons").add(
        PokemonBasicInfo(
            name = currentPokemon.name,
            imageUrl = currentPokemon.imageUrl,
            number = currentPokemon.number
        )
    )
    .addOnCompleteListener{ task ->
        if (task.isSuccessful) {
            navController.navigate(PokeTrainerScreens.HomeScreen.name)
        }
    }
    .addOnFailureListener { exception ->
            Log.e("DetailsScreen", "saveToFirebase() Fail")
    }
}

@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon,
    modifier: Modifier,
    colorOfTheType: Color,
    navController: NavController
) {
    val pokemonName = pokemonInfo.name!!
    val pokemonId = pokemonInfo.id!!
    val pokemonImageUrl = pokemonInfo.sprites!!.front_default
    val currentPokemon = PokemonBasicInfo(pokemonName, pokemonImageUrl, pokemonId)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "#${pokemonId} ${pokemonName.replaceFirstChar {it.uppercase() }}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        PokemonTypeSection(types = pokemonInfo.types!!)
        PokemonDetailDataSection(
            pokemonWeight = pokemonInfo.weight!!,
            pokemonHeight = pokemonInfo.height!!
        )
        PokemonBaseStats(pokemonInfo = pokemonInfo)
        PokemonSaveSection(colorOfTheType, currentPokemon, navController)
    }
}

@Composable
fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (type in types) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(type))
                    .height(27.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = type.type.name.replaceFirstChar { it.uppercase() },
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 60.dp
) {
    val pokemonWeightInKg = remember {
        pokemonWeight * 100f / 1000f
    }
    val pokemonHeightInMeters = remember {
        pokemonHeight * 100f / 1000f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetailsDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(
                id = R.drawable.ic_weight
            ),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.LightGray))
        PokemonDetailsDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(
                id = R.drawable.ic_height
            ),
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
fun PokemonDetailsDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = "Item image",
            tint = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colors.onSurface
        )

    }
}

@Composable
fun PokemonDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .background(
            Brush.verticalGradient(
                listOf(
                    Color.Black,
                    Color.Transparent
                )
            )
        ),
    contentAlignment = Alignment.TopStart) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "ArrowBack image",
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
//                    navController.popBackStack()
                    navController.navigate(PokeTrainerScreens.SearchScreen.name)
                }
        )
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var isAnimationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(targetValue = if (isAnimationPlayed) {
        statValue / statMaxValue.toFloat()
    } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        isAnimationPlayed = true
    }
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .clip(CircleShape)
        .background(
            Color.LightGray
        )) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PokemonBaseStats(
    pokemonInfo: Pokemon,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokemonInfo.stats!!.maxOf { it.baseStat }
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 6.dp)) {
        Text(
            text = "Base stats:",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))

        for (i in pokemonInfo.stats!!.indices) {
            val stat = pokemonInfo.stats[i]
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}