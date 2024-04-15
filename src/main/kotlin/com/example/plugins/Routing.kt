package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/books") {
            call.respondText("This book has taught me alot")
        }
    }
}
