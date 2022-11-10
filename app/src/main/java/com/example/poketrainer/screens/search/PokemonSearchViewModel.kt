package com.example.poketrainer.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poketrainer.data.Resource
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
    private var curPage = 0
    var pokemonList = mutableStateOf<List<PokemonBasicInfo>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokemonBasicInfo>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)
    var isSearchRequested = mutableStateOf(false)

    init {
        getAllPokemons()
    }

    fun searchSpecifiedPokemon(name: String) {
        viewModelScope.launch {
            isLoading.value = true
            isSearchRequested.value = true
            try {
                when (val pokemon = repository.getPokemon(name.lowercase())) {
                    is Resource.Success -> {
                        pokemonList.value = listOf(PokemonBasicInfo(
                            pokemon.data!!.name.toString().replaceFirstChar { it.uppercase() },
                            pokemon.data.sprites!!.front_default,
                            pokemon.data.id!!
                        ))
                        isLoading.value = false
                        loadError.value = ""
                    }
                    is Resource.Error -> {
                        isLoading.value = false
                        loadError.value = pokemon.message.toString()
                    }
                    else -> {
                        Log.e(tagForLogging, "XXX Unexpected behavior in getAllPokemons()")
                    }
                }
            } catch (exception: Exception) {
                isLoading.value = false
                Log.e(tagForLogging, "XXX searchSpecifiedPokemon(): ${exception.message.toString()}")
            }
        }
    }

    fun searchPokemonFromAlreadyLoadedList(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.name.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if (isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
            isSearchRequested.value = false
        }
    }

    fun getAllPokemons() {
        viewModelScope.launch {
            isLoading.value = true
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
                        loadError.value = ""
                        isLoading.value = false
                        pokemonList.value += pokemonListPerPage

                    }
                    is Resource.Error -> {
                        loadError.value = response.message.toString()
                        isLoading.value = false
                    }
                    else -> {
                        Log.e(tagForLogging, "Unexpected behavior in getAllPokemons()")
                    }
                }
            } catch (exception: Exception) {
                isLoading.value = false
                Log.e(tagForLogging, "getAllPokemons(): ${exception.message.toString()}")
            }
        }
    }
}