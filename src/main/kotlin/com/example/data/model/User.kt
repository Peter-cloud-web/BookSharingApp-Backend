package com.example.data.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_email:String,
    val user_name:String,
    val user_hash_password:String,
) : Principal
