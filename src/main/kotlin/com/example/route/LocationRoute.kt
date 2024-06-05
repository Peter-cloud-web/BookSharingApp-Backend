package com.example.route

import com.example.data.model.Response
import com.example.repository.LocationRepo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.LocationRoutes(
    db: LocationRepo
) {
    post("/v1/addLocation") {
        try {
            db.insertLocations()
            call.respond(HttpStatusCode.OK, Response(true, "Locations added successfully"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, Response(false, e.message ?: "Some problems occurred"))
        }
    }

    get("v1/getAllLocations") {
        try {
            val locations = db.getAllLocations()
            call.respond(HttpStatusCode.OK, locations)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Some problem occurred")
        }
    }

    get("/v1/getBooksByLocation/{location}") {
        try {
            val locationInput = call.parameters["location"].toString()
            val locationBooks = db.getBooksByLocation(locationInput)
            call.respond(HttpStatusCode.OK, locationBooks)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, e.message ?: "Some problem occurred")
        }
    }
}