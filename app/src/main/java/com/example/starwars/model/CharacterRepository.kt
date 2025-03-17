package com.example.starwars.model

import com.example.starwars.data.CharacterData
import com.example.starwars.network.StarWarsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okio.IOException
import org.json.JSONException
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val api: StarWarsApi
) {
//    private val api = RetrofitInstance.api
    private var nextPageUrl: String? = "https://swapi.dev/api/people/"

    fun fetchAllCharacterData(): Flow<List<CharacterData>?> {
        return flow {
            val response = api.getCharacters(nextPageUrl!!)
            emit(response.results)
            nextPageUrl = response.next
            if (nextPageUrl == null) {
                emit(null)
            }
//            Log.d(TAG, "Emitter Thread: ${Thread.currentThread()}")
        }
    }
}