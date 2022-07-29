package com.example.routes

import com.example.data.database.CommentEntry
import com.example.data.database.UserEntry
import com.example.data.requests.AddComment
import com.example.data.response.SimpleResponse
import com.example.repository.CommentRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.commentRoutes(
    commentRepository: CommentRepository
) {
    authenticate("jwt") {
        route("v1/comment")
        {

            get("/{id}") {
                val id = try {
                    call.parameters["id"]!!.toInt()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 1", null))
                    return@get
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    val comment = commentRepository.getComments(id)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "success", comment))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "2. Something happened !!", null))
                    return@get
                }
            }

            post("/add") {
                val parameters = try {
                    call.receive<AddComment>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "1. Something happened !!", null))
                    return@post
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    val comment = commentRepository.addComment(parameters, userId)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "success", comment))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "2. Something happened !!", null))
                    return@post
                }
            }


            put("/update") {
                val parameters = try {
                    call.receive<CommentEntry>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "1. Something happened !!", null))
                    return@put
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    val comment = commentRepository.editComment(parameters, userId)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "success", comment))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "2. Something happened !!", null))
                    return@put
                }
            }


            delete("/delete") {
                val parameters = try {
                    call.receive<CommentEntry>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "1. Something happened !!", null))
                    return@delete
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    val comment =
                        commentRepository.deleteComment(id = parameters.id, blogId = parameters.blogId, userID = userId)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "success", "deleted $comment"))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "2. Something happened !!", null))
                    return@delete
                }
            }
        }
    }
}