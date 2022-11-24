package com.example.poketrainer.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.example.poketrainer.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirestoreRepository)
    : ViewModel() {

    val TAG = "HomeViewModel"
    val isLoading = mutableStateOf(false)
    private var loadError = ""
    val pokemonList = mutableStateOf<List<PokemonBasicInfo>>(emptyList())
    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

    init {
        getAllPokemonsFromFirestore()
    }

    private fun getAllPokemonsFromFirestore() {
        isLoading.value = true
        viewModelScope.launch {
            when (val response = repository.getAllPokemonsFromFirestore()) {
                is Resource.Success -> {
                    pokemonList.value = response.data!!.filter { pokemonBasicInfo -> pokemonBasicInfo.userId == currentUserId }
                        .sortedBy { pokemonBasicInfo -> pokemonBasicInfo.number }
                    isLoading.value = false
                }
                is Resource.Error -> {
                    loadError = response.message!!
                    isLoading.value = false
                }
                else -> {
                    Log.e(TAG, "getAllPokemonsFromFirestore() Unexpected response from firestore")
                }
            }
        }
    }
}