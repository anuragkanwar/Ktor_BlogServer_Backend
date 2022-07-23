package com.example.routes

import com.example.data.database.UserEntry
import com.example.data.response.SimpleResponse
import com.example.repository.FavBlogRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.favBlogRoutes(
    favBlogRepository: FavBlogRepository
) {
    authenticate("jwt") {
        route("/v1/fav") {

            post("/add/{id}")
            {
                val parameters = try {
                    call.parameters["id"]!!.toInt()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 1", null))
                    return@post
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    favBlogRepository.addToFav(parameters, userId)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Fav added", null))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 2", null))
                    return@post
                }

            }


            delete("/del/{id}") {
                val parameters = try {
                    call.parameters["id"]!!.toInt()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 1", null))
                    return@delete
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    favBlogRepository.removeFomFav(parameters, userId)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "Fav deleted", null))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 2", null))
                    return@delete
                }
            }
        }
    }
}