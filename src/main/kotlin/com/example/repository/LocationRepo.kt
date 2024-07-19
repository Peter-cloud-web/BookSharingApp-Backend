package com.example.repository

import com.example.data.model.Book
import com.example.data.model.Category
import com.example.data.model.Location
import com.example.data.model.locationsList
import com.example.data.tables.BookTable
import com.example.data.tables.CategoryTable
import com.example.data.tables.LocationTable
import com.example.data.tables.UserTable
import com.example.repository.CategoryRepo.CategoryResponse
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll

class LocationRepo {


    suspend fun insertLocations() {
        dbQuery {
            LocationTable.batchInsert(locationsList) { location ->
                this[LocationTable.locationId] = location.id
                this[LocationTable.location] = location.location
            }
        }
    }

    suspend fun getAllLocations(): List<Location?> = dbQuery {
        LocationTable.selectAll().map { locationRow ->
            rowToCategory(locationRow)
        }
    }

    private fun rowToCategory(row: ResultRow): Location? {

        if (row == null) {
            return null
        }
        return Location(
            id = row[LocationTable.locationId],
            location = row[LocationTable.location]
        )
    }

    suspend fun getBooksByLocation(location: String) = dbQuery {
        (BookTable.innerJoin(LocationTable)).innerJoin(UserTable)
            .selectAll()
            .where {
                LocationTable.locationId.eq(BookTable.locationId) and LocationTable.location.eq(location)
            }.map { row ->
                rowToLocationResponse(row)
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

    private fun rowToLocationResponse(row: ResultRow): BookResponse {
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
}