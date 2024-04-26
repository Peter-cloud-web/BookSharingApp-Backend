package com.example.data.model

import io.ktor.client.statement.*
import io.netty.handler.codec.http.HttpStatusClass
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val success: Boolean,
    val message:String
)