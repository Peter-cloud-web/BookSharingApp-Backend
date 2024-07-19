package com.example.repository

import com.example.data.model.Book
import com.example.data.model.Category
import com.example.data.model.categoriesList
import com.example.data.tables.BookTable
import com.example.data.tables.CategoryTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*

class CategoryRepo {

    suspend fun insertCategories() {
        dbQuery {
            CategoryTable.batchInsert(categoriesList) { category ->
                this[CategoryTable.categoryId] = category.id
                this[CategoryTable.category] = category.category
            }
        }
    }

    suspend fun getAllCategories(): List<Category?> = dbQuery {
        CategoryTable.selectAll().map { categoryRow ->
            rowToCategory(categoryRow)
        }
    }

    @Serializable
    data class CategoryResponse(
        val bookOwner: String,
        val firstName: String,
        val lastName: String,
        val category: String,
        val postedAt: Long,
        val books: List<Book>
    )

    suspend fun getBooksByCategory(category: String) = dbQuery {
        (BookTable.innerJoin(CategoryTable)).innerJoin(UserTable)
            .selectAll()
            .where {
                CategoryTable.categoryId.eq(BookTable.categoryId) and CategoryTable.category.eq(category)
            }.map { row ->
                rowToCategoryResponse(row)
            }
    }

    @Serializable
    data class BookResponse(
        val owner: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val bookId: Int,
        val categoryId: Int,
        val timeOfCreation: Long,
        val book: Book,
    )

    private fun rowToCategoryResponse(row: ResultRow): BookResponse {
        val userName = row[UserTable.userName]
        val firstName = row[UserTable.firstName]
        val lastName = row[UserTable.lastName]
        val userEmail = row[UserTable.userEmail]
        val book = Book(
            title = row[BookTable.title],
            author = row[BookTable.author],
            category = row[BookTable.category],
            location = row[BookTable.location],
            page = row[BookTable.page],
            summary = row[BookTable.summary],
            bookImage = row[BookTable.bookImage],
            isAvailable = row[BookTable.isAvailable],
        )

        return BookResponse(
            bookId = row[BookTable.id],
            categoryId = row[BookTable.categoryId],
            timeOfCreation = row[BookTable.createdAt],
            owner = userName,
            firstName = firstName,
            lastName = lastName,
            email = userEmail,
            book = book,
        )
    }

    private fun rowToCategory(row: ResultRow): Category? {

        if (row == null) {
            return null
        }
        return Category(
            id = row[CategoryTable.categoryId],
            category = row[CategoryTable.category]
        )
    }
}
