package com.example.poketrainer.repository

import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.model.pokeList.PokeList
import com.example.poketrainer.network.PokeApi
import javax.inject.Inject

class PokeRepository @Inject constructor(private val api: PokeApi) {

    suspend fun getPokemon(name: String): Resource<Pokemon> {
        val pokemon: Pokemon = try {
            api.getPokemon(name)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }
        return Resource.Success(data = pokemon)
    }

    suspend fun getAllPokemons(limit: Int, offset: Int): Resource<PokeList> {
        val pokeList = try {
            api.getAllPokemons(limit, offset)
        } catch (exception: Exception) {
            return Resource.Error(message = exception.message.toString())
        }
        return Resource.Success(data = pokeList)
    }
}