package com.example.starwars.data

object DataProvider {
    fun characterList() : List<CharacterData> {
        return listOf(
            CharacterData("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male"),
            CharacterData("C-3PO", "167", "75", "n/a", "gold", "yellow", "112BBY", "n/a"),
            CharacterData("R2-D2", "96", "32", "n/a", "white, blue", "red", "33BBY", "n/a"),
            CharacterData("Darth Vader", "202", "136", "none", "white", "yellow", "41.9BBY", "male"),
            CharacterData("Leia Organa", "150", "49", "brown", "light", "brown", "19BBY", "female"),
            CharacterData("Owen Lars", "178", "120", "brown, grey", "light", "blue", "52BBY", "male"),
            CharacterData("Beru Whitesun lars", "165", "75", "brown", "light", "blue", "47BBY", "female"),
            CharacterData("R5-D4", "97", "32", "n/a", "white, red", "red", "unknown", "n/a"),
            CharacterData("Biggs Darklighter", "183", "84", "black", "light", "brown", "24BBY", "male"),
            CharacterData("Obi-Wan Kenobi", "182", "77", "auburn, white", "fair", "blue-gray", "57BBY", "male")
        )


    }

    fun character(): CharacterData {
        return CharacterData("Luke Skywalker", "172", "77", "blond", "fair", "blue", "19BBY", "male")
    }
}