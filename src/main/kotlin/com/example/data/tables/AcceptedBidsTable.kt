package com.example.data.tables

import org.jetbrains.exposed.sql.Table

object AcceptedBidsTable : Table() {
    val id = integer("acceptedidId")
    val bookId = integer("bookId").references(BookTable.id)
    val bidId = integer("bidId").references(SentBidsTable.id)
    val title = varchar("bookTitle",512)
    val author = varchar("bookAuthor",512)
    val pages = integer("bookPages")
    val summary = varchar("bookSummary",512)
}