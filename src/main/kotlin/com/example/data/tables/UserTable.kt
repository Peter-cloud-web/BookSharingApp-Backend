package com.example.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.Dual.index

object UserTable : Table() {

    val userId = integer("userId").autoIncrement()
    val userEmail = varchar("userEmail", 512)
    val firstName = varchar("firstName",512)
    val lastName = varchar("lastName",512)
    val userName = varchar("userName", 512).uniqueIndex()
    val profilePicture = binary("profilePicture")
    val userHashPassword = varchar("hashPassword", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(userEmail)

}
