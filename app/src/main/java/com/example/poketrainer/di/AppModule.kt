package com.example.poketrainer.di

import com.example.poketrainer.network.PokeApi
import com.example.poketrainer.repository.FirestoreRepository
import com.example.poketrainer.repository.PokeRepository
import com.example.poketrainer.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirestoreRepository() = FirestoreRepository()

    @Singleton
    @Provides
    fun providePokeRepository(api: PokeApi) = PokeRepository(api)

    @Singleton
    @Provides
    fun providePokemonApi(): PokeApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }

}