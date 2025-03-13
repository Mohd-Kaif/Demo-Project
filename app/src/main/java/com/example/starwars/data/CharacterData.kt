package com.example.starwars.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterData(
    val name: String,
    val height: String,
    val mass: String,
    val hair_color: String,
    val skin_color: String,
    val eye_color: String,
    val birth_year: String,
    val gender: String
) : Parcelable
