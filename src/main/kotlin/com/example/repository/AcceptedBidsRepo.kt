package com.example.repository

import com.example.data.tables.AcceptedBidsTable
import com.example.data.tables.BookTable
import com.example.data.tables.SentBidsTable
import com.example.data.tables.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import joptsimple.internal.Rows
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll

class AcceptedBidsRepo {
    @Serializable
    data class AcceptedBidsResponse(
        val title: String,
        val author: String,
        val pages: Int,
        val summary: String,
        val createdAt: Long,
        val bookOwner: String
    )

    val createdAt = System.currentTimeMillis()

    suspend fun getAcceptedBids(bidId: Int, email: String) {
        dbQuery {
            SentBidsTable.innerJoin(BookTable).innerJoin(UserTable)
                .selectAll()
                .where { SentBidsTable.id.eq(bidId) }
                .where { BookTable.userEmail.eq(email) }
                .map { row ->
                    rowToAcceptedBidsResponse(row)
                }
        }

    }

    private fun rowToAcceptedBidsResponse(row: ResultRow): AcceptedBidsResponse {
        val book = AcceptedBidsResponse(
            title = row[AcceptedBidsTable.title],
            author = row[AcceptedBidsTable.author],
            pages = row[AcceptedBidsTable.pages],
            summary = row[AcceptedBidsTable.summary],
            createdAt = createdAt,
            bookOwner = row[BookTable.userEmail]
        )
        return book
    }

}