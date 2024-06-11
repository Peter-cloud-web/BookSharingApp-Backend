package com.example.repository

import com.example.data.model.Book
import com.example.data.model.User
import com.example.data.tables.BookTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
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
        val postedAt: Long,
        val associatedBooks: List<Book>

    )

    suspend fun getUserDetailsByEmail(email: String) = dbQuery {
        (UserTable.innerJoin(BookTable))
            .selectAll()
            .where {
                BookTable.userEmail.eq(email) and UserTable.userEmail.eq(email)
            }.map { row ->
                rowToUserDetails(row)
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
            user_lastname = row[UserTable.lastName]
        )
    }

    private fun rowToUserDetails(row: ResultRow): UserDetails {
        val userName = row[UserTable.userName]
        val userEmail = row[UserTable.userEmail]
        val userFirstName = row[UserTable.firstName]
        val userLastName = row[UserTable.lastName]
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
        return UserDetails(userName, userEmail, userFirstName, userLastName, postedAt, books)
    }
}