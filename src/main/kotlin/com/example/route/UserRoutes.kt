package com.example.route

import com.example.authentication.JwtService
import com.example.data.model.LoginRequest
import com.example.data.model.RegisterRequest
import com.example.data.model.Response
import com.example.data.model.User
import com.example.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*


fun Route.UserRoutes(
    db: UserRepository,
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

    post("/v1/register") {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Some Fields"))
            return@post
        }

        try {
            val user =
                User(registerRequest.userEmail, hashFunction(registerRequest.userPassword), registerRequest.userName)
            db.addUser(user)
            call.respond(HttpStatusCode.OK, Response(true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problems occurred"))
        }
    }

    post("/v1/login") {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Fields"))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.userEmail)

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, Response(false, "Wrong email"))
            } else {
                if (user.user_hash_password == hashFunction(loginRequest.userPassword)) {
                    call.respond(HttpStatusCode.OK, Response(true, jwtService.generateToken(user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, Response(false, "Password incorrect"))
                }
            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problem occurred"))
        }
    }

}