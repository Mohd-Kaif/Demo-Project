package com.example.starwars.viewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starwars.data.CharacterData
import com.example.starwars.model.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel : ViewModel() {
    private val characterRepository: CharacterRepository = CharacterRepository()

//    private val _allCharacterData = MutableStateFlow<List<CharacterData>>(emptyList())
//    val allCharacterData: StateFlow<List<CharacterData>> = _allCharacterData

    private val _newCharacterData = MutableStateFlow<List<CharacterData>>(emptyList())
    val newCharacterData: StateFlow<List<CharacterData>> = _newCharacterData

//    private val _currentPage  = mutableIntStateOf(1)
//    val currentPage: State<Int> = _currentPage

    suspend fun getAllCharacterData() {
        viewModelScope.launch {
            try {
                val result = characterRepository.fetchAllCharacterData()
                _newCharacterData.value = result

            } catch (e: Exception) {
                Log.e(TAG, "Error getting all characters data ${e.message}")
                e.printStackTrace()
            }
        }
    }

//    val homeUiState: StateFlow<HomeUiState> =
//        characterRepository.fetchAllCharacterData().map {  }
}

data class HomeUiState(val itemList: List<CharacterData> = listOf())