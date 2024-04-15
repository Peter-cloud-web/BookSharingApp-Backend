package com.example.repository

import com.example.data.model.User
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

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

    suspend fun findUserById(id:Int) = dbQuery {
        UserTable.selectAll().where{
            UserTable.userId.eq(id)
        }
            .map { row ->
                rowToUser(row)
            }
            .singleOrNull()
    }

    private fun rowToUser(row:ResultRow?):User?{
        if(row == null){
            return null
        }
        return User(
            user_email = row[UserTable.userEmail],
            user_hash_password = row[UserTable.userHashPassword],
            user_name = row[UserTable.userName]
        )
    }
}