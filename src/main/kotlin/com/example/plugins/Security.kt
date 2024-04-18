package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.authentication.JwtService
import com.example.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtRealm = "Note server"
    val jwtService = JwtService()
    val db = UserRepository()
    authentication {
        jwt("jwt") {
            realm = jwtRealm
            verifier(jwtService.varifier)
            validate { credential ->
                val payload = credential.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }
        }
    }
}
