package com.example.starwars.viewModel

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.data.CharacterData
import com.example.starwars.di.hasNetwork
import com.example.starwars.model.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _homeUiState = MutableStateFlow<Result<List<CharacterData>>>(Result.Loading)
    val homeUiState: StateFlow<Result<List<CharacterData>>> = _homeUiState

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var hasMoreData = true

    fun getAllCharacterData() {
        if (!hasMoreData) return
        viewModelScope.launch(Dispatchers.IO) {
//            if (hasNetwork() == false) return@launch
            characterRepository.fetchAllCharacterData()
                .onStart { _isLoading.value = true }
                .onCompletion { _isLoading.value = false }
                .catch { e ->
                    Log.e(TAG, "Error: ${e.message}")
                    _homeUiState.value = when (e) {
                        is SocketTimeoutException -> { Result.Error(Exception("Network Timeout. Please try again.")) }
                        is IOException -> { Result.Error(Exception("No Internet Connection")) }
                        is JSONException -> { Result.Error(Exception("Error parsing response."))}
                        is HttpException -> {
                            val errorMessage = when(e.code()) {
                                400 -> "Bad Request. Please try again."
                                401 -> "Unauthorized access. Check your credentials."
                                404 -> "Not Found. The resource doesn't exist."
                                500 -> "Server Error. Please try later."
//                                Handle 504 error for Unsatisfiable Request (only-if-cached) -> Occurs when we try to load after cached data is loaded
                                else -> "Unexpected Error: ${e.code()}"
                            }
                            Result.Error(Exception(errorMessage))
                        }
                        else -> { Result.Error(e) }
                    }
                }
                .collect { result ->
                    val currentList = (_homeUiState.value as? Result.Success)?.data ?: emptyList()
                    if (result == null) {
                        hasMoreData = false
                    } else {
                        _homeUiState.value = Result.Success(currentList + result)
                    }
                }
        }
    }
}

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable?) : Result<Nothing>
    object Loading: Result<Nothing>
}