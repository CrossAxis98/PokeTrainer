package com.example.poketrainer.screens.search

import android.text.method.TextKeyListener.Capitalize
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.example.poketrainer.repository.PokeRepository
import com.example.poketrainer.utils.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor(private val repository: PokeRepository):
    ViewModel() {

    private val tagForLogging = "PokemonSearchViewModel"
    var pokemon: Pokemon? by mutableStateOf(Pokemon())
    var isLoading by mutableStateOf(true)

    private var curPage = 0
    var pokemonList = mutableStateOf<List<PokemonBasicInfo>>(listOf())
    var loadErrorAllPokemons = mutableStateOf("")
    var isLoadingAllPokemons = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonBasicInfo>()

    init {
        //loadPokemon()
        getAllPokemons()
    }

    private fun loadPokemon() {
        searchPokemon("jynx")
    }

    fun getAllPokemons() {
        viewModelScope.launch {
            isLoadingAllPokemons.value = true
            try {
                when (val response = repository.getAllPokemons(PAGE_SIZE, curPage * PAGE_SIZE)) {
                    is Resource.Success -> {
                        endReached.value = curPage * PAGE_SIZE >= response.data!!.count
                        val pokemonListPerPage: List<PokemonBasicInfo> = response.data.results.mapIndexed { index, pokemon ->
                            val number = if (pokemon.url.endsWith("/")) {
                                pokemon.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                pokemon.url.takeLastWhile { it.isDigit() }
                            }
                            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                            PokemonBasicInfo(
                                pokemon.name.replaceFirstChar {
                                    it.uppercase()
                                },
                                url,
                                number.toInt()
                            )
                        }
                        curPage++
                        loadErrorAllPokemons.value = ""
                        isLoadingAllPokemons.value = false
                        pokemonList.value += pokemonListPerPage

                    }
                    is Resource.Error -> {
                        loadErrorAllPokemons.value = response.message.toString()
                        isLoadingAllPokemons.value = false
                    }
                    else -> {
                        Log.d(tagForLogging, "Unexpected behavior in getAllPokemons()")
                    }
                }

            } catch (exception: Exception) {
                isLoadingAllPokemons.value = false
                Log.d(tagForLogging, "getAllPokemons(): ${exception.message.toString()}")
            }
        }
    }

    fun searchPokemon(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (name.isEmpty()) return@launch
            try {
                when (val response = repository.getPokemon(name)) {
                    is Resource.Success -> {
                        pokemon = response.data!!
                        if (pokemon != null) isLoading = false
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e(tagForLogging, "searchPokemon(): Failed getting pokemon")
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                Log.d(tagForLogging, "searchPokemon(): ${e.message.toString()}")
            }
        }
    }
}