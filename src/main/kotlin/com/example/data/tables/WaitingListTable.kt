package com.example.data.tables

import com.example.data.tables.BookTable.references
import org.jetbrains.exposed.sql.Table

object WaitingListTable : Table() {
    val id = integer("waitingListId").autoIncrement()
    val userEmail = varchar("userEmail",512).references(UserTable.userEmail)

    override val primaryKey: PrimaryKey = PrimaryKey(id)

    init {
         integer("book_fk")
            .uniqueIndex()
            .references(BookTable.id)
    }
}