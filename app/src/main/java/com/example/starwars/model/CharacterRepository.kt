package com.example.starwars.model

import android.util.Log
import com.example.starwars.data.CharacterData
import com.example.starwars.network.StarWarsApi
import com.example.starwars.viewModel.Result
import com.example.starwars.viewModel.asResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val api: StarWarsApi
) {
//    private val api = RetrofitInstance.api
    private var nextPageUrl: String? = "https://swapi.dev/api/people/"

    suspend fun fetchAllCharacterData(): Flow<Result<CharacterData>> {
        return flow {
            if (nextPageUrl == null) {
                throw Exception()
            }
            val response = api.getCharacters(nextPageUrl!!)
            response.results.forEach {
                emit(it)
//                delay(500)
            }
            nextPageUrl = response.next
//            Log.d(TAG, "Emitter Thread: ${Thread.currentThread()}")
        }.asResult()
    }
}

