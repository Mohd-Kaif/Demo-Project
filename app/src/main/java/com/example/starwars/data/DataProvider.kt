package com.example.starwars.data

object DataProvider {
    fun characterList() : List<CharacterData> {
        return List(10) {character()}
    }

    fun character(): CharacterData {
        return CharacterData("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male")
    }
}