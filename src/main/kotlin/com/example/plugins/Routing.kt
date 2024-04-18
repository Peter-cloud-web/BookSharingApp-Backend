package com.example.plugins

import com.example.authentication.JwtService
import com.example.authentication.hashing
import com.example.data.model.User
import com.example.repository.UserRepository
import com.example.route.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val jwtService = JwtService()
    val hashFunction = {s:String -> hashing(s) }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        UserRoutes(jwtService,hashFunction)
    }
}
