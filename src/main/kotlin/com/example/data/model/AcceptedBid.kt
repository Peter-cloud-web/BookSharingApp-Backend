package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AcceptedBid(
val title:String,
val author:String,
val pages:Int,
val summary:String,
)

