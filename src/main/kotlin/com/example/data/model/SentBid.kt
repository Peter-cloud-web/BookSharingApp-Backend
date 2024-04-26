package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SentBid(
    val title:String,
    val author:String,
    val pages:Int,
    val summary:String,
)
