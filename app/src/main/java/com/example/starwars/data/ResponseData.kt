package com.example.starwars.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ResponseData(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results : List<CharacterData>?,
)
