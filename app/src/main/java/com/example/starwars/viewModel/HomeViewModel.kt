package com.example.starwars.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.data.CharacterData
import com.example.starwars.model.CharacterRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {
    private val characterRepository: CharacterRepository = CharacterRepository()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState

//    private val _allCharactersList = MutableStateFlow<List<CharacterData>>(emptyList())
//    val allCharactersList: StateFlow<List<CharacterData>> = _allCharactersList

    fun getAllCharacterData() {
        _homeUiState.value = _homeUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val result = async { characterRepository.fetchAllCharacterData() }
                val updatedList = homeUiState.value.itemList + result.await()
                _homeUiState.value = HomeUiState(
                    itemList = updatedList,
                    isLoading = false,
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error getting all characters data ${e.message}")
                _homeUiState.value = _homeUiState.value.copy(
                    isLoading = false,
                    error = "Failed to load characters. Please try again."
                )
            }
        }
    }
}

data class HomeUiState(
    val itemList: List<CharacterData> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
)