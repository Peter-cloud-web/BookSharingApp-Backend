package com.example.repository

import com.example.data.model.Book
import com.example.data.model.User
import com.example.data.model.User2
import com.example.data.tables.BookTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class UserRepository {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[UserTable.userEmail] = user.user_email
                userTable[UserTable.firstName] = user.user_firstname
                userTable[UserTable.lastName] = user.user_lastname
                userTable[UserTable.userName] = user.user_name
                userTable[UserTable.profilePicture] = user.profilePicture
                userTable[UserTable.userHashPassword] = user.user_hash_password
            }

        }
    }

    @Serializable
    data class UserDetails(
        val userEmail: String,
        val userFirstName: String,
        val userLastName: String,
        val userName: String,
        val associatedBooks:List<Book>
    )

    suspend fun getUserDetailsByEmail(email: String) = dbQuery {
        val user = UserTable.selectAll().where(UserTable.userEmail.eq(email)).singleOrNull()?.let { row -> rowToUser(row) }

        user?.let { user ->
            val books = BookTable.selectAll()
                .where{BookTable.userEmail.eq(email)}
                .map { row -> rowToUserDetails(row) }

            UserDetails(
                userEmail = user.user_email,
                userFirstName = user.user_firstname,
                userLastName = user.user_lastname,
                userName = user.user_name,
                associatedBooks = books
            )

        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.selectAll().where {
            UserTable.userEmail.eq(email)
        }
            .map { row ->
                rowToUser(row)
            }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }
        return User(
            user_email = row[UserTable.userEmail],
            user_hash_password = row[UserTable.userHashPassword],
            user_name = row[UserTable.userName],
            user_firstname = row[UserTable.firstName],
            profilePicture = row[UserTable.profilePicture],
            user_lastname = row[UserTable.lastName]
        )
    }

    private fun rowToUserDetails(row: ResultRow): Book {
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
        return book
    }
}