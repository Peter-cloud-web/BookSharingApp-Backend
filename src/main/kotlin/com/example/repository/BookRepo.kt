package com.example.repository

import com.example.data.model.Book
import com.example.data.model.Category
import com.example.data.model.User
import com.example.data.tables.BookTable
import com.example.data.tables.CategoryTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class BookRepo {
    val currentCreationTime = System.currentTimeMillis()
    suspend fun postBook(book: Book, email: String) {

        val categoryId = getCategoryByName(book.category)?.id ?: throw IllegalArgumentException("Category not found")
        dbQuery {
            BookTable.insert { bookTable ->
                bookTable[BookTable.userEmail] = email
                bookTable[BookTable.page] = book.page
                bookTable[BookTable.category] = book.category
                bookTable[BookTable.categoryId] = categoryId
                bookTable[BookTable.author] = book.author
                bookTable[BookTable.title] = book.title
                bookTable[BookTable.summary] = book.summary
                bookTable[BookTable.isAvailable] = book.isAvailable
                bookTable[BookTable.createdAt] = currentCreationTime
            }
        }
    }

    @Serializable
    data class BookResponse(
        val owner: String,
        val bookId: Int,
        val categoryId: Int,
        val timeOfCreation: Long,
        val book: Book,
    )

    suspend fun getAllBooks(): List<BookResponse> = dbQuery {

        (BookTable.innerJoin(UserTable))
            .selectAll()
            .where {
                BookTable.userEmail.eq(UserTable.userEmail)
            }
            .map { bookRow ->
                rowToBookResponse(bookRow)
            }
    }


    private suspend fun getCategoryByName(categoryName: String): Category? = dbQuery {
        CategoryTable.selectAll().where { CategoryTable.category.eq(categoryName) }
            .map { rowToCategory(it) }
            .singleOrNull()
    }


    private fun rowToCategory(row: ResultRow): Category {
        return Category(
            id = row[CategoryTable.categoryId],
            category = row[CategoryTable.category]
        )
    }

    private fun rowToBookResponse(row: ResultRow): BookResponse {
        val userName = row[UserTable.userName]
        val book = Book(
            title = row[BookTable.title],
            author = row[BookTable.author],
            category = row[BookTable.category],
            page = row[BookTable.page],
            summary = row[BookTable.summary],
            isAvailable = row[BookTable.isAvailable],
        )

        return BookResponse(
            bookId = row[BookTable.id],
            categoryId = row[BookTable.categoryId],
            timeOfCreation = row[BookTable.createdAt],
            owner = userName,
            book = book,

        )

    }


}

