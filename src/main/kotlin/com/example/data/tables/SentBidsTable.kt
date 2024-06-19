package com.example.data.tables

import com.example.data.tables.ReceivedBidsTable.references
import org.jetbrains.exposed.sql.Table

object SentBidsTable : Table() {

    val id = integer("bidId").autoIncrement()
    val title = varchar("bookTitle", 512)
    val bidderEmail = varchar("bidderEmail", 512).references(UserTable.userEmail)
    val bookOwnerEmail = varchar("bookOwnerEmail",512)
    val bookId = integer("bookId").references(BookTable.id)
    val author = varchar("bookAuthor", 512)
    val page = integer("bookPages")
    val summary = varchar("bookSummary", 1000)
    val sentAt = long("createdAt")
    val isBidAccepted = bool("isBidAccepted")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}