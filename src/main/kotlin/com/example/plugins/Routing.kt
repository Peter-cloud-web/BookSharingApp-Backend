package com.example.plugins

import com.example.authentication.JwtService
import com.example.authentication.hashing
import com.example.data.model.User
import com.example.repository.*
import com.example.route.*
import io.ktor.server.application.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.swagger.codegen.v3.generators.html.StaticHtmlCodegen

fun Application.configureRouting() {
    val  db = UserRepository()
    val repo = BookRepo()
    val bidRepo = BidRepo()
    val categoryRepo = CategoryRepo()
    val locationRepo = LocationRepo()
    val jwtService = JwtService()
    val hashFunction = {s:String -> hashing(s) }
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        get("/") {
            call.respondText("Hello World!")
        }
        UserRoutes(db,jwtService,hashFunction)
        BookRoutes(repo)
        CategoryRoutes(categoryRepo)
        BidRoute(bidRepo)
        LocationRoutes(locationRepo)
    }
}
