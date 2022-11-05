package com.example.poketrainer.model.pokeList

data class PokeList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)