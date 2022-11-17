package com.example.poketrainer.repository

import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreRepository {
    suspend fun getAllPokemonsFromFirestore(): Resource<List<PokemonBasicInfo>> {
        return suspendCoroutine { continuation ->
            var pokemonList: List<PokemonBasicInfo>
            val db = Firebase.firestore
            try {
                db.collection("pokemons").get()
                    .addOnSuccessListener { documents ->
                        pokemonList = documents.map { documentSnapshot ->
                            documentSnapshot.toObject(PokemonBasicInfo::class.java)
                        }
                        continuation.resume(Resource.Success(data = pokemonList))
                    }
            } catch (exception: Throwable) {
                continuation.resumeWithException(exception)
            }
        }
    }
}