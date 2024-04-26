package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val userEmail:String,
    val userPassword:String,
)
