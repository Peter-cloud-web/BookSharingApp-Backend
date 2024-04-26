package com.example.repository

import com.example.data.model.Book
import com.example.data.model.Category
import com.example.data.model.categoryList
import com.example.data.tables.BookTable
import com.example.data.tables.CategoryTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class CategoryRepo {

    suspend fun insertCategories() {

        dbQuery {
            CategoryTable.batchInsert(categoryList) { category ->
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

    private fun rowToCategoryResponse(row: ResultRow): CategoryResponse {
        val bookOwner = row[UserTable.userName]
        val category = row[CategoryTable.category]
        val postedAt = row[BookTable.createdAt]
        val book = Book(
            title = row[BookTable.title],
            author = row[BookTable.author],
            category = row[BookTable.category],
            page = row[BookTable.page],
            summary = row[BookTable.summary],
            isAvailable = row[BookTable.isAvailable],
        )

        val books = listOf(book)

        return CategoryResponse(bookOwner, category, postedAt, books)

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
