package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val userEmail:String,
    val userName:String,
    val userPassword:String

)
