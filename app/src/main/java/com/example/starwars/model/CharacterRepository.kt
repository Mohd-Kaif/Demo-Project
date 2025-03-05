package com.example.starwars.model

import android.util.Log
import com.example.starwars.data.CharacterData
import com.example.starwars.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.w3c.dom.Attr

private const val TAG = "CharacterRepository"

class CharacterRepository {
    private val api = RetrofitInstance.api

//    suspend fun fetchCharacterData(page: Int): CharacterData {
//        return withContext(Dispatchers.IO) {
//            api.getCharacters(page)
//        }
//        return CharacterData(1, "Luke Skywalker", "19 BBY", "Male", "172", "Blue", "Blond")
//    }

    suspend fun fetchAllCharacterData(): List<CharacterData> {
        val result : MutableList<CharacterData> = mutableListOf()
        try {
            val response = withContext(Dispatchers.IO) {
                api.getCharacters()
            }
            result.addAll(response.results)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching data")
        }
        return result
//        return listOf(
//            CharacterData(1, "Luke Skywalker", "19 BBY", "Male", "172", "Blue", "Blond"),
//            CharacterData(1, "C-3PO", "19 BBY", "Male", "172", "Blue", "Blond"),
//            CharacterData(1, "R2-D2", "19 BBY", "Male", "172", "Blue", "Blond"),
//            CharacterData(1, "Darth Vader", "19 BBY", "Male", "172", "Blue", "Blond"),
//            CharacterData(1, "Luke Skywalker", "19 BBY", "Male", "172", "Blue", "Blond"),
//            CharacterData(1, "Luke Skywalker", "19 BBY", "Male", "172", "Blue", "Blond"),
//        )
    }
}