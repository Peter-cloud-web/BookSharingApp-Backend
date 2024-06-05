package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val id:Int,
    val location: String
)
