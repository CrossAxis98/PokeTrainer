package com.example.poketrainer.repository

import com.example.poketrainer.data.Resource
import com.example.poketrainer.model.Pokemon
import com.example.poketrainer.model.pokeList.PokeList
import com.example.poketrainer.network.PokeApi
import javax.inject.Inject

class PokeRepository @Inject constructor(private val api: PokeApi) {

    suspend fun getPokemon(name: String): Resource<Pokemon> {
        val pokemon: Pokemon = try {
            //Resource.Loading(data = true)
            api.getPokemon(name)
        } catch (exception: Exception) {
//            Resource.Loading(data = false)
            return Resource.Error(message = exception.message.toString())
        }
        //Resource.Loading(data = false)
        return Resource.Success(data = pokemon)
    }

    suspend fun getAllPokemons(limit: Int, offset: Int): Resource<PokeList> {
        val pokeList = try {
//            Resource.Loading(data = true)
            api.getAllPokemons(limit, offset)
        } catch (exception: Exception) {
//            Resource.Loading(data = false)
            return Resource.Error(message = exception.message.toString())
        }
//        Resource.Loading(data = false)
        return Resource.Success(data = pokeList)
    }
}