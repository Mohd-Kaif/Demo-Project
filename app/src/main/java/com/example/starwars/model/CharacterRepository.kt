package com.example.starwars.model

import com.example.starwars.BASE_URL
import com.example.starwars.data.CharacterData
import com.example.starwars.network.StarWarsApi
import com.example.starwars.viewModel.Result
import com.example.starwars.viewModel.asResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val api: StarWarsApi
) {
    private var nextPageUrl: String? = BASE_URL

    fun fetchAllCharacterData(): Flow<Result<List<CharacterData>>> {
        return flow {
            if (nextPageUrl == null) return@flow
            val response = api.getCharacters(nextPageUrl!!)
            emit(response.results)
            nextPageUrl = response.next
        }.asResult()
            .flowOn(Dispatchers.IO)
            .catch { e->
                val errMessage = when(e) {
                    is SocketTimeoutException -> "Network Timeout. Please try again."
                    is IOException -> "No Internet Connection"
                    is JSONException -> "Error parsing response."
                    is HttpException -> "No Internet Connection."
                    else -> "Please try again."
                }
                emit(Result.Error(Exception(errMessage)))
            }
    }
}