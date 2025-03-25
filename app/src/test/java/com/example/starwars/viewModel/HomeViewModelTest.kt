package com.example.starwars.viewModel

import com.example.starwars.data.DataProvider
import com.example.starwars.model.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private val characterRepository = mockk<CharacterRepository>()
    private val testDispatcher = StandardTestDispatcher()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        viewModel = HomeViewModel(characterRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test_getAllCharactersData_updatesHomeUiStateToSuccess() = runTest {
        val expected = DataProvider.characterList()
        val flow = flow { emit(Result.Success(expected)) }
        coEvery { characterRepository.fetchAllCharacterData(false) } returns flow

        viewModel.getAllCharacterData(false)
        flow.collect()
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.homeUiState.value is Result.Success)

        val actual = (viewModel.homeUiState.value as Result.Success).data

        assertEquals(expected, actual)
    }

    @Test
    fun test_getAllCharactersData_updatesHomeUiStateToError() = runTest {
        val expected = Exception("response not found")
        val flow = flow { emit(Result.Error(expected)) }
        coEvery { characterRepository.fetchAllCharacterData(false) } returns flow

        viewModel.getAllCharacterData(false)
        flow.collect()
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.homeUiState.value is Result.Error)

        val actual = (viewModel.homeUiState.value as Result.Error).exception

        assertEquals(actual, expected)
    }

    @Test
    fun test_getAllCharacterData_noUpdateOnErrorWhenListIsNotEmpty() = runTest {
        val expected = DataProvider.characterList()
        run {
            val flow = flow { emit(Result.Success(expected)) }
            coEvery { characterRepository.fetchAllCharacterData(false) } returns flow

            viewModel.getAllCharacterData(false)
            flow.collect()
            testDispatcher.scheduler.advanceUntilIdle()
        }
        assert((viewModel.homeUiState.value as Result.Success).data.isNotEmpty())
        val flow = flow { emit(Result.Error(Exception("Oops!! Something went wrong"))) }
        coEvery { characterRepository.fetchAllCharacterData(false) } returns flow

        viewModel.getAllCharacterData(false)
        flow.collect()
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.homeUiState.value is Result.Success)
        val actual = (viewModel.homeUiState.value as Result.Success).data
        assertEquals(actual, expected)
    }
}