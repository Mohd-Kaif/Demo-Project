package com.example.starwars.model

import com.example.starwars.data.DataProvider
import com.example.starwars.data.ResponseData
import com.example.starwars.network.StarWarsApi
import com.example.starwars.viewModel.Result
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CharacterRepositoryTest {
    private val api: StarWarsApi = mockk()
    private lateinit var mockRepository: CharacterRepository

    @Before
    fun setUp() {
        mockRepository = CharacterRepository(api)
    }

    @Test
    fun testFetchAllCharactersData_returnsEmptyList() = runTest {
        val mockData = ResponseData(count = 1, next = null, previous = null, results = emptyList())
        coEvery { api.getCharacters(any()) } returns mockData

        val response = mockRepository.fetchAllCharacterData(false)
        response.onEach { res->
            assert(res is Result.Success)
            assertEquals(mockData.results, (res as Result.Success).data)
        }.launchIn(this)
    }

    @Test
    fun testFetchAllCharactersData_returnsCharacterList() = runTest {
        val mockData = ResponseData(count = 1, next = null, previous = null, results = DataProvider.characterList())
        coEvery { api.getCharacters(any()) } returns mockData

        val response = mockRepository.fetchAllCharacterData(false)
        response.onEach { res->
            assertTrue(res is Result.Success)
            assertEquals(10, (res as Result.Success).data.size)
            assertEquals("Luke Skywalker", res.data[0].name)
        }.launchIn(this)
    }

    @Test
    fun testFetchAllCharactersData_returnsNoResponseError() = runTest {
        val mockData: ResponseData? = null
        coEvery { api.getCharacters(any()) } returns mockData

        val response = mockRepository.fetchAllCharacterData(false)
        response.onEach { res->
            assert(res is Result.Error)
            assertEquals("response not found", (res as Result.Error).exception?.message)
        }.launchIn(this)
    }

    @Test
    fun testFetchAllCharactersData_returnsResponseButNoDataError() = runTest {
        val mockData = ResponseData(count = 1, next = null, previous = null, results = null)
        coEvery { api.getCharacters(any()) } returns mockData

        val response = mockRepository.fetchAllCharacterData(false)
        response.onEach { res->
            assert(res is Result.Error)
            assertEquals("data not found in response", (res as Result.Error).exception?.message)
        }.launchIn(this)
    }

    @Test
    fun testFetchAllCharactersData_returnsNetworkError() = runTest {
        val exception = Exception("Network Error")
        coEvery { api.getCharacters(any()) } throws exception

        val response = mockRepository.fetchAllCharacterData(false)
        response.onEach { res->
            assert(res is Result.Error)
            assertEquals(exception.message, (res as Result.Error).exception?.message)
        }.launchIn(this)
    }
}