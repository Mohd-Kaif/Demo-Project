package com.example.starwars.model

import android.util.Log
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
import retrofit2.http.HTTP
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "CharacterRepository"

class CharacterRepository @Inject constructor(
    private val api: StarWarsApi
) {
    private var nextPageUrl: String? = BASE_URL

    fun fetchAllCharacterData(refresh: Boolean): Flow<Result<List<CharacterData>>> {
        if (refresh) {
            nextPageUrl = BASE_URL
        }
        return flow {
            if (nextPageUrl == null) return@flow
            val response = api.getCharacters(nextPageUrl!!) ?: throw Exception("response not found")
            if (response.results == null) {
                throw Exception("data not found in response")
            }
            emit(response.results)
            nextPageUrl = response.next
        }.asResult()
            .catch { e->
                val errMessage = when(e) {
                    is SocketTimeoutException -> "Request Timed Out. Please try again."
                    is IOException -> "No Internet Connection"
                    is JSONException -> "Something went wrong"
                    is HttpException -> when(val code = e.code()) {
                        504 -> "No Internet Connection"
                        else -> "HTTP $code error Something went wrong"
                    }
//                    else -> "Oops!! Something went wrong"
                    else -> e.message
                }
                emit(Result.Error(Exception(errMessage)))
            }
            .flowOn(Dispatchers.IO)
    }
}