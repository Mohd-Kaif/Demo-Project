package com.example.starwars.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
//    @SerialName("birth_year")
    val birth_year: String,
    val gender: String,
    val height: String,
//    @SerialName("eye_color")
    val eye_color: String,
//    @SerialName("hair_color")
    val hair_color: String,
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