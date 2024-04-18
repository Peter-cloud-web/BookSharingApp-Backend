package com.example.route

import com.example.authentication.JwtService
import com.example.data.model.User
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.UserRoutes(
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    get("/token") {
        val email = call.request.queryParameters["email"]!!
        val password = call.request.queryParameters["password"]!!
        val username = call.request.queryParameters["username"]!!

        val user = User(email, hashFunction(password), username)
        call.respond(jwtService.generateToken(user))
    }

}