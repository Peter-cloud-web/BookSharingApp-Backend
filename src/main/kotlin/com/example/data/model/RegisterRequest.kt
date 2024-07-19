package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val userEmail:String,
    val firstName:String,
    val lastName:String,
    val userName:String,
    val profilePicture:ByteArray,
    val userPassword:String

)
