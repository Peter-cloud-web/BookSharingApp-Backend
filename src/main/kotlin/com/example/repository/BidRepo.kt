package com.example.repository

import com.example.data.model.Book
import com.example.data.model.SentBid
import com.example.data.tables.BookTable
import com.example.data.tables.SentBidsTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*


class BidRepo {
    val currentCreationTime = System.currentTimeMillis()

    @Serializable
    data class SentBidsResponse(
        val biddedBookOwner: String,
//        val biddedFirstName: String,
//        val biddedLastName: String,
        val biddedBookId: Int,
        val biddedBook: List<Book>,
        val book: List<SentBid>
    )

    @Serializable
    data class ReceivedBidsResponse(
        val bidder: String,
//        val bidderFirstName: String,
//        val bidderLastName: String,
        val biddedBookId: Int,
        val biddedBook: List<Book>,
        val book: List<SentBid>
    )


    suspend fun sendBid(userEmail: String, bookId: Int, bid: SentBid) {
        dbQuery {
            val ownerEmail =
                BookTable.select(BookTable.userEmail).where { BookTable.id.eq(bookId) }
                    .firstOrNull()?.get(BookTable.userEmail)

            SentBidsTable.insert { bidRow ->
                bidRow[SentBidsTable.bidderEmail] = userEmail
                if (ownerEmail != null) {
                    bidRow[SentBidsTable.bookOwnerEmail] = ownerEmail
                }
                bidRow[SentBidsTable.bookId] = bookId
                bidRow[SentBidsTable.author] = bid.author
                bidRow[SentBidsTable.page] = bid.pages
                bidRow[SentBidsTable.title] = bid.title
                bidRow[SentBidsTable.summary] = bid.summary
                bidRow[SentBidsTable.sentAt] = currentCreationTime
            }
        }
    }

    suspend fun getSentBids(email: String): List<SentBidsResponse> = dbQuery {

        (SentBidsTable.innerJoin(BookTable))
            .selectAll()
            .where {
                SentBidsTable.bidderEmail.eq(email)
            }
            .map { row ->
                rowToSentBidResponse(row)
            }
    }

    suspend fun getReceivedBids(email: String): List<ReceivedBidsResponse> = dbQuery {
        (SentBidsTable.innerJoin(BookTable))
            .selectAll()
            .where {
                SentBidsTable.bookOwnerEmail.eq(email)
            }.map { row ->
                rowToReceivedBidResponse(row)
            }
    }
}

private fun rowToSentBidResponse(row: ResultRow): BidRepo.SentBidsResponse {

    var bidder = row[SentBidsTable.bookOwnerEmail]
//    val biddedFirstName = row[UserTable.firstName]
//    val biddedLastName = row[UserTable.lastName]
    var biddedBookId = row[BookTable.id]
    val book = Book(
        title = row[BookTable.title],
        author = row[BookTable.author],
        category = row[BookTable.category],
        location = row[BookTable.location],
        page = row[BookTable.page],
        summary = row[BookTable.summary],
        isAvailable = row[BookTable.isAvailable]
    )


    val sentBidBook = SentBid(
        title = row[SentBidsTable.title],
        author = row[SentBidsTable.author],
        pages = row[SentBidsTable.page],
        summary = row[SentBidsTable.summary]
    )

    val books = listOf(book)
    val bids = listOf(sentBidBook)

    return BidRepo.SentBidsResponse(bidder, biddedBookId, books, bids)

}

private fun rowToReceivedBidResponse(row: ResultRow): BidRepo.ReceivedBidsResponse {

    var biderBookOwner = row[SentBidsTable.bidderEmail]
//    val bidderFirstName = row[UserTable.firstName]
//    val bidderLastName = row[UserTable.lastName]
    var bidderBookId = row[BookTable.id]
    val book = Book(
        title = row[BookTable.title],
        author = row[BookTable.author],
        category = row[BookTable.category],
        location = row[BookTable.location],
        page = row[BookTable.page],
        summary = row[BookTable.summary],
        isAvailable = row[BookTable.isAvailable]
    )


    val sentBidBook = SentBid(
        title = row[SentBidsTable.title],
        author = row[SentBidsTable.author],
        pages = row[SentBidsTable.page],
        summary = row[SentBidsTable.summary]
    )

    val books = listOf(book)
    val bids = listOf(sentBidBook)

    return BidRepo.ReceivedBidsResponse(biderBookOwner, bidderBookId, books, bids)

}

