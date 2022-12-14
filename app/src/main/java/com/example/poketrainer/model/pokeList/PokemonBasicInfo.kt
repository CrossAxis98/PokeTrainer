package com.example.poketrainer.model.pokeList

data class PokemonBasicInfo(
    val name: String = "",
    val imageUrl: String = "",
    val number: Int = 0,
    var isMarkedAsWannaCatch: Boolean = false,
    var isMarkedAsCaught: Boolean = false,
    var dateOfCatch: String = "01 sty 1970",
    var userId: String = ""
)
