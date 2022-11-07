package com.example.poketrainer.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.poketrainer.model.Stat
import com.example.poketrainer.model.Type
import com.example.poketrainer.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ShowBars(isRequiredToShowBars: Boolean) {
    rememberSystemUiController().apply {
        this.isSystemBarsVisible = isRequiredToShowBars
        this.isStatusBarVisible = isRequiredToShowBars
        this.isNavigationBarVisible = isRequiredToShowBars
    }
}

fun getColorByPokemonType(name: String): Color {
    return when (name) {
        "fire" -> Color.Red
        "bug" -> Color(0xFF0F3800)
        "dark" -> Color.Black
        "dragon" -> Color(0xFFFF5722)
        "electric" -> Color.Yellow
        "fairy" -> Color.Magenta
        "fighting" -> Color.DarkGray
        "flying" -> Color.LightGray
        "ghost" -> Color(0xFF9600FF)
        "grass" -> Color.Green
        "ground" -> Color(0xFF864A15)
        "ice" -> Color(0xFF00F6FF)
        "normal" -> Color(0xFFD1DFCB)
        "poison" -> Color(0xFF782AC7)
        "psychic" -> Color(0xFFAF9C16)
        "rock" -> Color(0xFF352202)
        "steel" -> Color.Gray
        "water" -> Color(0xFF1592FF)
        else -> Color.White
    }
}

fun parseTypeToColor(type: Type): Color {
    return when(type.type.name.replaceFirstChar { it.lowercase() }) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.replaceFirstChar { it.lowercase() }) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.replaceFirstChar { it.lowercase() }) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}