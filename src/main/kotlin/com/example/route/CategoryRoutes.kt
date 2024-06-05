package com.example.route

import com.example.data.model.Category
import com.example.data.model.Response
import com.example.repository.CategoryRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*

fun Route.CategoryRoutes(
    db: CategoryRepo
) {
    post("/v1/addCategories") {
        try {
            db.insertCategories()
            call.respond(HttpStatusCode.OK, Response(true, "Categories added successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problems occurred"))
        }
    }

    get("/v1/getAllCategories") {
        try {
            val category = db.getAllCategories()
            call.respond(HttpStatusCode.OK, category)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problems occurred"))
        }
    }

    get("/v1/getBooksByCategory/{category}") {
        try {
            val bookCategory = call.parameters["category"].toString()
            val category = db.getBooksByCategory(bookCategory)
            call.respond(HttpStatusCode.OK, category)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Some problem occurred")
        }
    }

}

