package com.example.data.tables

import com.example.data.model.User
import org.jetbrains.exposed.sql.Table

object AcceptedBidsTable : Table() {
    val id = integer("acceptedBidId").autoIncrement()
    val bookId = integer("bookId").references(BookTable.id)
    val bidId = integer("bidId").references(SentBidsTable.id)
    val bidSenderId = varchar("bidSenderId",512).references(UserTable.userEmail)
    val bidAcceptorId = varchar("bidAcceptorEmail",512)
    val title = varchar("bookTitle", 512)
    val author = varchar("bookAuthor", 512)
    val pages = integer("bookPages")
    val summary = varchar("bookSummary", 512)
    val acceptanceState = bool("bidState")
    val createdAt = long("createdAt")

    override val primaryKey: Table.PrimaryKey = PrimaryKey(id)
}