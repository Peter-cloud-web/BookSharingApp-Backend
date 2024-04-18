package com.example.data.model

import io.ktor.client.statement.*
import io.netty.handler.codec.http.HttpStatusClass

data class Response(
    val success: Boolean,
    val message:String
)