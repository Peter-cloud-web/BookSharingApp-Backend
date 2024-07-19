package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val title:String,
    val bookImage:ByteArray,
    val author:String,
    val category:String,
    val location:String,
    val page:Int,
    val summary:String,
    val isAvailable:Boolean,
)

@Serializable
data class Book2(
    val title:String,
    val author:String,
    val category:String,
    val location:String,
    val page:Int,
    val summary:String,
    val isAvailable:Boolean,
)
