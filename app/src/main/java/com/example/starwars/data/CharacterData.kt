package com.example.starwars.data

import com.google.gson.annotations.SerializedName


data class ResponseData(
    val count: Int,
    val next: String,
    val previous: String,
    val results : ArrayList<CharacterData>,
)

data class CharacterData(
    val name: String,
    @SerializedName("birth_year")
    val birthYear: String,
    val gender: String,
    val height: String,
    @SerializedName("eye_color")
    val eyeColor: String,
    @SerializedName("hair_color")
    val hairColor: String,
)