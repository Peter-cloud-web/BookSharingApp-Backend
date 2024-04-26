package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val title:String,
    val author:String,
    val category:String,
    val page:Int,
    val summary:String,
    val isAvailable:Boolean,
)
