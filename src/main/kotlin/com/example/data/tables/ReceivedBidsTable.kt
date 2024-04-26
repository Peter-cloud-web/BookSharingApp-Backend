package com.example.data.tables

import com.example.data.tables.SentBidsTable.autoIncrement
import com.example.data.tables.SentBidsTable.references
import org.jetbrains.exposed.sql.Table

object ReceivedBidsTable: Table() {

    val id = integer("bidId").autoIncrement()
    val bidderEmail = varchar("bidderEmail", 512).references(UserTable.userEmail)
    val bookOwnerEmail = varchar("bookOwnerEmail",512)
    val sentBid = integer("sentBidId").references(SentBidsTable.id)
    val bookId = integer("bookId").references(BookTable.id)
    val title = varchar("bookTitle", 512)
    val author = varchar("bookAuthor", 512)
    val page = integer("bookPages")
    val summary = varchar("bookSummary", 1000)
    val sentAt = long("createdAt")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}