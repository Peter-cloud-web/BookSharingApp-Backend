package com.example.plugins

import com.example.authentication.JwtService
import com.example.authentication.hashing
import com.example.data.model.User
import com.example.repository.BidRepo
import com.example.repository.BookRepo
import com.example.repository.CategoryRepo
import com.example.repository.UserRepository
import com.example.route.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val  db = UserRepository()
    val repo = BookRepo()
    val bidRepo = BidRepo()
    val categoryRepo = CategoryRepo()
    val jwtService = JwtService()
    val hashFunction = {s:String -> hashing(s) }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        UserRoutes(db,jwtService,hashFunction)
        BookRoutes(repo)
        CategoryRoutes(categoryRepo)
        BidRoute(bidRepo)
    }
}
