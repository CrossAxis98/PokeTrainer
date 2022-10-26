package com.example.poketrainer.network

import com.example.poketrainer.model.Pokemon
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface PokeApi {

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String) : Pokemon

}