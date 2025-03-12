package com.example.starwars.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.data.CharacterData
import com.example.starwars.model.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
import java.io.IOException
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

//    init {
//        getAllCharacterData()
//    }

    fun getAllCharacterData() {
        viewModelScope.launch {
            try {
                characterRepository.fetchAllCharacterData()
                    .onStart {
                        _homeUiState.value = Result.Loading
                        _isLoading.value = true
                    }
                    .onCompletion { _isLoading.value = false }
                    .flowOn(Dispatchers.IO)
                    .collect { result ->
//                        Log.d(TAG, "Collector Thread: ${Thread.currentThread()}")
                        when (result) {
                            is Result.Success -> {
                                val currentList = (_homeUiState.value as? Result.Success)?.data ?: emptyList()
                                _homeUiState.value = Result.Success(currentList + result.data)
                            }
                            is Result.Error -> {
                                _homeUiState.value = Result.Error(result.exception)
                            }
                            Result.Loading -> {_homeUiState.value = Result.Loading }
                        }
                    }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}")
                _homeUiState.value = Result.Error(e)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching characters: ${e.message}")
                _homeUiState.value = Result.Error(e)
            }
            _isLoading.value = false
        }
    }
}

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable?) : Result<Nothing>
    object Loading: Result<Nothing>
}

fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}