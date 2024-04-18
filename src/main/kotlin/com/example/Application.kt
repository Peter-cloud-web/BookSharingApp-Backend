 package com.example

import com.example.authentication.JwtService
import com.example.authentication.hashing
import com.example.plugins.*
import com.example.repository.DatabaseFactory
import com.example.repository.UserRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    DatabaseFactory.init()

    configureSerialization()
    configureSecurity()
    configureRouting()
    configureHTTP()
}
