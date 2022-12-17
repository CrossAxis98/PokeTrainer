package com.example.poketrainer.repository

import android.util.Log
import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.pokeList.PokemonBasicInfo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirestoreRepository {
    val TAG = "FirestoreRepository"
    suspend fun getAllPokemonsFromFirestore(): Resource<MutableList<PokemonBasicInfo>> {
        return suspendCoroutine { continuation ->
            var pokemonList: MutableList<PokemonBasicInfo>
            val db = Firebase.firestore
            try {
                db.collection("pokemons").get()
                    .addOnSuccessListener { documents ->
                        pokemonList = documents.map { documentSnapshot ->
                            documentSnapshot.toObject(PokemonBasicInfo::class.java)
                        }.toMutableList()
                        continuation.resume(Resource.Success(data = pokemonList))
                    }
            } catch (exception: Throwable) {
                continuation.resumeWithException(exception)
            }
        }
    }

    suspend fun removePokemonFromFirestore(id: Int): Resource<Boolean> {
        return suspendCoroutine { continuation ->
            val db = Firebase.firestore
            try {

                db.collection("pokemons").document(id.toString())
                    .delete()
                    .addOnSuccessListener {
                        continuation.resume(Resource.Success(data = true))
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Failed to remove pokemon with id $id")
                        continuation.resume(Resource.Error(data = false, message = "Failure in removePokemonFromFirestore()"))
                    }
            } catch (exception: Throwable) {
                continuation.resumeWithException(exception)
            }
        }
    }
}