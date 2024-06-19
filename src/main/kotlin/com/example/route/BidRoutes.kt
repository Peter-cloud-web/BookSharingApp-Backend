package com.example.route

import com.example.data.model.Response
import com.example.data.model.SentBid
import com.example.data.model.User
import com.example.repository.BidRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.BidRoute(bidRepo: BidRepo) {

    authenticate("jwt") {
        post("/v1/sendBid/{bookId}") {

            val bookId = call.parameters["bookId"]?.toIntOrNull()!!
            val bid = try {
                call.receive<SentBid>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, Response(false, "Missing Fields"))
                return@post
            }

            try {
                val email = call.principal<User>()!!.user_email
                bidRepo.sendBid(email, bookId, bid)
                call.respond(HttpStatusCode.OK, Response(true, "Bid sent successfully"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problem occurred"))
            }
        }

        get("/v1/sentBids") {
            try {
                val email = call.principal<User>()!!.user_email
                val bids = bidRepo.getSentBids(email)
                call.respond(HttpStatusCode.OK, bids)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, e.localizedMessage ?: emptyList<BidRepo.SentBidsResponse>())
            }
        }

        get("/v1/receivedBids") {
            try {
                val email = call.principal<User>()!!.user_email
                val bids = bidRepo.getReceivedBids(email)
                call.respond(HttpStatusCode.OK, bids)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, emptyList<BidRepo.SentBidsResponse>())
            }

        }

        post("/v1/acceptBid/{bidId}") {
            val bidId = call.parameters["bidId"]?.toIntOrNull()
            if (bidId == null) {
                call.respond(HttpStatusCode.BadRequest, Response(false, "Invalid bid ID"))
                return@post
            }
            try {
                val email = call.principal<User>()!!.user_email
                val acceptedBid = bidRepo.acceptedBid(bidId, email)
                call.respond(HttpStatusCode.OK, Response(true, "Bid accepted successfully,  ${acceptedBid}"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Failed to accept bid"))
            }
        }

        get("/v1/getAcceptedBids") {
            try {
                val email = call.principal<User>()!!.user_email
                val acceptedBids = bidRepo.getAllAcceptedBids(email)
                call.respond(HttpStatusCode.OK, acceptedBids)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, emptyList<BidRepo.AcceptedBidResponse>())
            }
        }
    }

}