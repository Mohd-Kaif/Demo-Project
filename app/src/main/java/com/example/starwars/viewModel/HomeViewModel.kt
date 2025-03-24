package com.example.starwars.viewModel

import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.R
import com.example.starwars.data.CharacterData
import com.example.starwars.model.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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

    fun getAllCharacterData(refresh: Boolean) {
        viewModelScope.launch {
            characterRepository.fetchAllCharacterData(refresh)
                .onStart { _isLoading.value = true }
                .onCompletion { _isLoading.value = false }
                .collect { result ->
                    val currentList =
                        if ((_homeUiState.value !is Result.Success) || refresh) emptyList()
                        else (_homeUiState.value as Result.Success).data
                    when (result) {
                        is Result.Success -> {
                            _homeUiState.value = Result.Success(currentList + result.data)
                        }
                        is Result.Error -> {
                            if (currentList.isEmpty()) {
                                _homeUiState.value = Result.Error(result.exception)
                            }
                        }
                        is Result.Loading -> {}
                    }
                }
        }
    }

    fun shareDetails(context: Context, characterData: CharacterData) {
        val shareText =
            """
            |Name: ${characterData.name}
            |Height: ${characterData.height}cm
            |Gender: ${characterData.gender}
            |Birth Year: ${characterData.birth_year}
            |Eye Color: ${characterData.eye_color}
            |Hair Color: ${characterData.hair_color}
        """.trimMargin()
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/*"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.getString(R.string.app_name)
            )
        )
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
}