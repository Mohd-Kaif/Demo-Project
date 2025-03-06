package com.example.starwars.network

import com.example.starwars.data.ResponseData
import retrofit2.http.GET
import retrofit2.http.Url

interface StarWarsApi {
    @GET
    suspend fun getCharacters(@Url url: String): ResponseData
}