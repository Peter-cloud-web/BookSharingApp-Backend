package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object UserTable:Table(){

    val userId = integer("userId").autoIncrement()
    val userEmail = varchar("userEmail",512)
    val userName = varchar("userName", 512)
    val userHashPassword = varchar("hashPassword",512)

    override val primaryKey: PrimaryKey = PrimaryKey(userId)

}
