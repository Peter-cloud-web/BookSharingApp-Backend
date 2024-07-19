package com.example.repository

import com.example.data.model.*
import com.example.data.tables.BookTable
import com.example.data.tables.CategoryTable
import com.example.data.tables.LocationTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.Base64

class BookRepo {
    val currentCreationTime = System.currentTimeMillis()
    suspend fun postBook(book: Book, email: String) {

        val categoryId = getCategoryByName(book.category)?.id ?: throw IllegalArgumentException("Category not found")
        val locationId = getLocationByName(book.location)?.id ?: throw IllegalArgumentException("Location not found")
        dbQuery {
            BookTable.insert { bookTable ->
                bookTable[BookTable.userEmail] = email
                bookTable[BookTable.page] = book.page
                bookTable[BookTable.bookImage] = book.bookImage
                bookTable[BookTable.category] = book.category
                bookTable[BookTable.categoryId] = categoryId
                bookTable[BookTable.locationId] = locationId
                bookTable[BookTable.location] = book.location
                bookTable[BookTable.author] = book.author
                bookTable[BookTable.title] = book.title
                bookTable[BookTable.summary] = book.summary
                bookTable[BookTable.isAvailable] = book.isAvailable
                bookTable[BookTable.createdAt] = currentCreationTime
            }
        }
    }

    @Serializable
    data class BookResponse2(
        val owner: String,
        val firstName:String,
        val lastName:String,
        val email: String,
        val bookId: Int,
        val categoryId: Int,
        val timeOfCreation: Long,
        val book: Book2,
    )

    @Serializable
    data class BookResponse(
        val owner: String,
        val firstName:String,
        val lastName:String,
        val email: String,
        val bookId: Int,
        val categoryId: Int,
        val timeOfCreation: Long,
        val book: Book,
    )

    @Serializable
    data class ImageResponse(
        val bookImage: ByteArray,
    )

    @Serializable
    data class ProfilePicResponse(
        val profilePic: ByteArray,
    )



    suspend fun getAllBooks(): List<BookResponse2> = dbQuery {

        (BookTable.innerJoin(UserTable)
            .slice(BookTable.columns + UserTable.columns))
            .selectAll()
            .where {
                BookTable.userEmail.eq(UserTable.userEmail)
            }
            .orderBy(BookTable.createdAt, SortOrder.ASC)
            .map { bookRow ->
                rowToBookResponse(bookRow)
            }
    }

    suspend fun getBookById(id: Int): BookResponse? = dbQuery {
        (BookTable.innerJoin(UserTable)).selectAll()
            .where(BookTable.id.eq(id))
            .map { row -> rowToBookResponse2(row) }
            .singleOrNull()
    }
    
    suspend fun getUserAvatar(userEmail:String):ProfilePicResponse? = dbQuery {
        (BookTable.innerJoin(UserTable)).selectAll()
            .where(BookTable.userEmail.eq(userEmail))
            .map { row -> rowToUserImage(row) }
            .singleOrNull()
    }


    private suspend fun getCategoryByName(categoryName: String): Category? = dbQuery {
        CategoryTable.selectAll().where { CategoryTable.category.eq(categoryName) }
            .map { rowToCategory(it) }
            .singleOrNull()
    }

    private suspend fun getLocationByName(location: String): Location? = dbQuery {
        LocationTable.selectAll().where { LocationTable.location.eq(location) }
            .map { rowToLocation(it) }
            .singleOrNull()
    }

    private fun rowToBookImageResponse(row: ResultRow): ImageResponse{
        val bookImage = row[BookTable.bookImage]
        return ImageResponse(bookImage = bookImage)
    }

    private fun rowToUserImage(row:ResultRow):ProfilePicResponse{
        val userProfilePhoto = row[UserTable.profilePicture]
        return ProfilePicResponse(profilePic = userProfilePhoto)
    }


    private fun rowToCategory(row: ResultRow): Category {
        return Category(
            id = row[CategoryTable.categoryId],
            category = row[CategoryTable.category]
        )
    }

    private fun rowToLocation(row: ResultRow): Location {
        return Location(
            id = row[LocationTable.locationId],
            location = row[LocationTable.location]
        )
    }





    private fun rowToBookResponse(row: ResultRow): BookResponse2 {
        val userName = row[UserTable.userName]
        val firstName = row[UserTable.firstName]
        val lastName = row[UserTable.lastName]
        val userEmail = row[UserTable.userEmail]
        val book = Book2(
            title = row[BookTable.title],
            author = row[BookTable.author],
            category = row[BookTable.category],
            location = row[BookTable.location],
            page = row[BookTable.page],
            summary = row[BookTable.summary],
            isAvailable = row[BookTable.isAvailable],
        )

        return BookResponse2(
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


    private fun rowToBookResponse2(row: ResultRow): BookResponse {
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

