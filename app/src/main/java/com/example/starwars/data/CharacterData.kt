package com.example.starwars.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseData(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results : ArrayList<CharacterDto>,
)

@Serializable
data class CharacterDto(
    val name: String,
    @SerialName("birth_year")
    val birthYear: String,
    val gender: String,
    val height: String,
    @SerialName("eye_color")
    val eyeColor: String,
    @SerialName("hair_color")
    val hairColor: String,
)

@Parcelize
data class CharacterData(
    val name: String,
    val birthYear: String,
    val gender: String,
    val height: String,
    val eyeColor: String,
    val hairColor: String
) : Parcelable

fun CharacterDto.toCharacterData(): CharacterData {
    return CharacterData(
        name = this.name,
        birthYear = this.birthYear,
        gender = this.gender,
        height = this.height,
        eyeColor = this.eyeColor,
        hairColor = this.hairColor
    )
}