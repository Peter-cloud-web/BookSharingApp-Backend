package com.example.route

import com.example.data.model.Book
import com.example.data.model.Response
import com.example.data.model.User
import com.example.repository.BookRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*

fun Route.BookRoutes(
    repo: BookRepo
) {

    authenticate("jwt") {
        post("/v1/create") {
            val book = try {
                call.receive<Book>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Fields"))
                return@post
            }
            try {
                val email = call.principal<User>()!!.user_email
                repo.postBook(book, email)
                call.respond(HttpStatusCode.OK, Response(true, "Book posted successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problems occurred"))
            }
        }  

        get("/v1/allBooks") {
            try {
                val books = repo.getAllBooks()
                call.respond(HttpStatusCode.OK, books)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, emptyList<BookRepo.BookResponse>())
            }
        }


        get("/v1/getBooksById/{bookId}") {
            try {
                val bookId = call.parameters["bookId"]?.toInt()
                val book = bookId?.let { it1 -> repo.getBookById(it1) }
                if (book != null) {
                    call.respond(HttpStatusCode.OK, book)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Book not found")
                }
            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Internal server error")
            }
        }
    }
}