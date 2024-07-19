package com.example.repository

import com.example.data.model.Book
import com.example.data.model.SentBid
import com.example.data.tables.AcceptedBidsTable
import com.example.data.tables.BookTable
import com.example.data.tables.SentBidsTable
import com.example.repository.DatabaseFactory.dbQuery
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class BidRepo {
    val currentCreationTime = System.currentTimeMillis()
    val acceptedBids = mutableListOf<AcceptedBidResponse>()

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
        val bidId: Int,
//        val bidderFirstName: String,
//        val bidderLastName: String,
        val biddedBookId: Int,
        val isBidAccepted: Boolean,
        val biddedBook: List<Book>,
        val book: List<SentBid>
    )


    @Serializable
    data class AcceptedBidResponse(
        val biddedBookId: Int,
        val isBidAccepted: Boolean,
        val bidSenderId:String,
        val bidAcceptorId: String,
        val bookTitle: String,
        val bookAuthor: String,
        val bookPage: Int,
        val bookSummary: String,
        val createdAt: Long,
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
                bidRow[SentBidsTable.isBidAccepted] = false
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

    suspend fun acceptedBid(bidId: Int, email: String): AcceptedBidResponse {
        return dbQuery {
            val sentBidRow = SentBidsTable.innerJoin(BookTable)
                .select(SentBidsTable.id.eq(bidId))
                .singleOrNull() ?: throw IllegalArgumentException("Bid not found")

            val biddedBookId = sentBidRow[BookTable.id]
            val bookRow = BookTable
                .select(BookTable.id eq biddedBookId)
                .singleOrNull() ?: throw IllegalArgumentException("Book not found")

            val insertedId = AcceptedBidsTable.insert { bidRow ->
                bidRow[AcceptedBidsTable.bidId] = bidId
                bidRow[AcceptedBidsTable.bookId] = biddedBookId
                bidRow[AcceptedBidsTable.bidSenderId] = sentBidRow[SentBidsTable.bidderEmail]
                bidRow[AcceptedBidsTable.bidAcceptorId] = email
                bidRow[AcceptedBidsTable.title] = sentBidRow[SentBidsTable.title]
                bidRow[AcceptedBidsTable.author] = sentBidRow[SentBidsTable.author]
                bidRow[AcceptedBidsTable.pages] = sentBidRow[SentBidsTable.page]
                bidRow[AcceptedBidsTable.summary] = sentBidRow[SentBidsTable.summary]
                bidRow[AcceptedBidsTable.acceptanceState] = true
                bidRow[AcceptedBidsTable.createdAt] = System.currentTimeMillis()
            } get AcceptedBidsTable.id

            AcceptedBidsTable
                .select(AcceptedBidsTable.id eq insertedId)
                .single()
                .let { rowToAcceptedBidResponse(it) }
        }
    }


    suspend fun getAllAcceptedBids(email: String): List<AcceptedBidResponse> = dbQuery {
        AcceptedBidsTable
            .selectAll()
            .where {
                AcceptedBidsTable.bidAcceptorId.eq(email)
            }
            .map { rows ->
                rowToAcceptedBidResponse(rows)
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

private fun rowToAcceptedBidResponse(row: ResultRow): BidRepo.AcceptedBidResponse {
    return BidRepo.AcceptedBidResponse(
        biddedBookId = row[AcceptedBidsTable.id],
        isBidAccepted = true,
        bidSenderId = row[AcceptedBidsTable.bidSenderId],
        bidAcceptorId = row[AcceptedBidsTable.bidAcceptorId],
        bookTitle = row[AcceptedBidsTable.title],
        bookAuthor = row[AcceptedBidsTable.author],
        bookPage = row[AcceptedBidsTable.pages],
        bookSummary = row[AcceptedBidsTable.summary],
        createdAt = row[AcceptedBidsTable.createdAt]
    )
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
        bookImage = row[BookTable.bookImage],
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

    var bidId = row[SentBidsTable.id]
    var biderBookOwner = row[SentBidsTable.bidderEmail]
    var isBidAccepted = row[SentBidsTable.isBidAccepted]
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
        bookImage = row[BookTable.bookImage],
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

    return BidRepo.ReceivedBidsResponse(biderBookOwner, bidId, bidderBookId, isBidAccepted, books, bids)

}

//private fun rowToAcceptedBid(row: ResultRow): BidRepo.AcceptedBidResponse {
//
//    val createdAt = System.currentTimeMillis()
//    return BidRepo.AcceptedBidResponse(
//        biddedBookId = row[SentBidsTable.id],
//        isBidAccepted = true,
//        bookTitle = row[SentBidsTable.title],
//        bookAuthor = row[SentBidsTable.author],
//        bookPage = row[SentBidsTable.page],
//        bookSummary = row[SentBidsTable.summary],
//        createdAt = createdAt
//    )


