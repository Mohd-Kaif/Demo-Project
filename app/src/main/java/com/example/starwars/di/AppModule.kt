package com.example.starwars.di

import com.example.starwars.model.CharacterRepository
import com.example.starwars.network.StarWarsApi
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

//    @Provides
//    @Singleton
//    fun provideRetrofit(): Retrofit = Retrofit.Builder()
//        .baseUrl("https://swapi.dev/api/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit): StarWarsApi =
//        retrofit.create(StarWarsApi::class.java)

    @Provides
    fun provideRepository(apiService: StarWarsApi): CharacterRepository {
        return CharacterRepository(apiService)
    }
}
