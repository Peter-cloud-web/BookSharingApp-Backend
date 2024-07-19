package com.example.route

import com.example.data.model.Book
import com.example.data.model.Response
import com.example.data.model.User
import com.example.data.tables.BookTable
import com.example.repository.BookRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import java.util.*
import kotlin.NoSuchElementException

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

        get("/v1/book/{id}/image"){
            val bookId = call.parameters["id"]?.toInt()
            if (bookId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
                return@get
            }
            val book = repo.getBookById(bookId)
            if (book == null) {
                call.respond(HttpStatusCode.NotFound, "Book not found")
                return@get
            }
            call.respondBytes(book.book.bookImage, ContentType.Image.PNG)
        }

    get("/v1/profile/{email}/image"){
        val userEmail = call.parameters["email"]?.toString()
        if (userEmail == null) {
            call.respond(HttpStatusCode.BadRequest, "Invalid user email")
            return@get
        }
        val picture = repo.getUserAvatar(userEmail)
        if (picture == null) {
            call.respond(HttpStatusCode.NotFound, "Picture not found")
            return@get
        }
        call.respondBytes(picture.profilePic, ContentType.Image.PNG)
    }
}