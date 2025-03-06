package com.example.starwars.viewModel

import androidx.lifecycle.ViewModel
import com.example.starwars.data.CharacterData
import com.example.starwars.model.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Thread.State
import javax.inject.Inject
//
//class CharacterDetailViewModel @Inject constructor(
//    private val repository: CharacterRepository
//):ViewModel() {
//    private val _characterDetailsUiState = MutableStateFlow(CharacterDetailsUiState())
//    val characterDetailsUiState: StateFlow<CharacterDetailsUiState> = _characterDetailsUiState
//}
//
//data class CharacterDetailsUiState (
//    val characterData: CharacterData? = null
//)
