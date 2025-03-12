package com.example.starwars.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseData(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results : ArrayList<CharacterData>,
) : Parcelable

@Parcelize
data class CharacterData(
    val name: String,
    val height: String,
    val mass: String,
    val hair_color: String,
    val skin_color: String,
    val eye_color: String,
    val birth_year: String,
    val gender: String,
//    @SerialName("birth_year")
//    @SerialName("eye_color")
//    @SerialName("hair_color")
) : Parcelable

//@Parcelize
//data class CharacterData(
//    val name: String,
//    val birthYear: String,
//    val gender: String,
//    val height: String,
//    val eyeColor: String,
//    val hairColor: String
//) : Parcelable

//fun CharacterDto.toCharacterData(): CharacterData {
//    return CharacterData(
//        name = this.name,
//        birthYear = this.birthYear,
//        gender = this.gender,
//        height = this.height,
//        eyeColor = this.eyeColor,
//        hairColor = this.hairColor
//    )
//}