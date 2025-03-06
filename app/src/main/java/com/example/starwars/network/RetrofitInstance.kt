package com.example.starwars.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/") // Base URL for API
            .addConverterFactory(GsonConverterFactory.create()) // JSON Parser
            .build()
    }

    @Provides
    @Singleton
    fun provideStarWarsApi(retrofit: Retrofit): StarWarsApi {
        return retrofit.create(StarWarsApi::class.java)
    }
}


//object RetrofitInstance {
//    private const val BASE_URL = "https://swapi.dev/api/"
//
//    val api: StarWarsApi by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(StarWarsApi::class.java)
//    }
//}