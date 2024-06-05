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
    data class LocationResponse(
        val bookOwner: String,
        val category: String,
        val postedAt: Long,
        val books: List<Book>
    )

    private fun rowToLocationResponse(row: ResultRow): LocationResponse {
        val bookOwner = row[UserTable.userName]
        val category = row[CategoryTable.category]
        val postedAt = row[BookTable.createdAt]
        val book = Book(
            title = row[BookTable.title],
            author = row[BookTable.author],
            category = row[BookTable.category],
            location = row[BookTable.location],
            page = row[BookTable.page],
            summary = row[BookTable.summary],
            isAvailable = row[BookTable.isAvailable],
        )

        val books = listOf(book)

        return LocationResponse(bookOwner, category, postedAt, books)

    }
}