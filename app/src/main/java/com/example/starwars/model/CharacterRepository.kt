package com.example.starwars.model

import android.util.Log
import com.example.starwars.data.CharacterData
import com.example.starwars.network.StarWarsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val api: StarWarsApi
) {
//    private val api = RetrofitInstance.api
    private var nextPageUrl: String? = "https://swapi.dev/api/people/"

    suspend fun fetchAllCharacterData(): List<CharacterData> {
        val result : MutableList<CharacterData> = mutableListOf()
        if (nextPageUrl == null) return result

        try {
            val response = withContext(Dispatchers.IO) {
                api.getCharacters(nextPageUrl!!)
            }
            result.addAll(response.results)
            nextPageUrl = response.next
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching data")
        }
        return result
    }
}