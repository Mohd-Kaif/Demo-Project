package com.example.starwars.network

import com.example.starwars.data.CharacterData
import com.example.starwars.data.ResponseData
import retrofit2.http.GET
import retrofit2.http.Query

interface StarWarsApi {
    @GET("people/")
    suspend fun getCharacters(): ResponseData
}