package com.example.poketrainer.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: PokeRepository)
    : ViewModel() {

//    private val tagForLogging = "PokemonSearchViewModel"
//    var pokemon: Pokemon? by mutableStateOf(Pokemon())
//    var isLoading by mutableStateOf(true)

    suspend fun getPokemonDetails(name: String) = repository.getPokemon(name)

//    fun getPokemonDetails(name: String): Resource<Pokemon> {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (name.isEmpty()) return@launch
//            try {
//                when (val response = repository.getPokemon(name)) {
//                    is Resource.Success -> {
//                        pokemon = response.data!!
//                        if (pokemon != null) isLoading = false
//                    }
//                    is Resource.Error -> {
//                        isLoading = false
//                        Log.e(tagForLogging, "searchPokemon(): Failed getting pokemon")
//                    }
//                }
//            } catch (e: Exception) {
//                isLoading = false
//                Log.d(tagForLogging, "searchPokemon(): ${e.message.toString()}")
//            }
//        }
//    }
}