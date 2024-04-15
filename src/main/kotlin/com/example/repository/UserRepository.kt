package com.example.repository

import com.example.data.model.User
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.insert

class UserRepository {

    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { userTable ->
                userTable[UserTable.userId]
                userTable[UserTable.userEmail] = user.user_email
                userTable[UserTable.userName] = user.user_name
                userTable[UserTable.userHashPassword] = user.user_hash_password
            }

        }
    }
}