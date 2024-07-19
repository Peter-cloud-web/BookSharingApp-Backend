package com.example.data.model

import com.example.data.tables.BookTable.binary
import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val user_email:String,
    val user_firstname:String,
    val user_lastname:String,
    val user_name:String,
    val profilePicture : ByteArray,
    val user_hash_password:String,
) : Principal


@Serializable
data class User2(
    val user_email:String,
    val user_firstname:String,
    val user_lastname:String,
    val user_name:String,
    val user_hash_password:String,
) : Principal
