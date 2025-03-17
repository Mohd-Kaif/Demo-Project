package com.example.starwars.model

import android.util.Log
import com.example.starwars.data.CharacterData
import com.example.starwars.network.StarWarsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import com.example.starwars.viewModel.Result
import com.example.starwars.viewModel.asResult
import okio.IOException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val api: StarWarsApi
) {
//    private val api = RetrofitInstance.api
    private var nextPageUrl: String? = "https://swapi.dev/api/people/"

    fun fetchAllCharacterData(): Flow<Result<List<CharacterData>>> {
        return flow {
            if (nextPageUrl == null) {
                return@flow
            }
            val response = api.getCharacters(nextPageUrl!!)
            Log.d(TAG, "${response.results.size}")
            emit(response.results)
            nextPageUrl = response.next
//            Log.d(TAG, "Emitter Thread: ${Thread.currentThread()}")
        }.asResult()
            .catch { e->
                Log.e(TAG, "${e.message}")
                emit(when (e) {
                    is SocketTimeoutException -> { Result.Error(Exception("Network Timeout. Please try again.")) }
                    is IOException -> { Result.Error(Exception("No Internet Connection")) }
                    is JSONException -> { Result.Error(Exception("Error parsing response."))}
                    is HttpException -> { Result.Error(Exception("No Internet Connection.")) }
                    else -> { Result.Error(e) }
                })
            }
    }
}