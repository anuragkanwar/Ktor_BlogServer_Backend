package com.example.routes

import com.example.data.database.UserEntry
import com.example.data.requests.CreateBlog
import com.example.data.response.SimpleResponse
import com.example.repository.BlogRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.blogRoutes(
    blogRepository: BlogRepository
) {
    authenticate("jwt") {

        route("/v1/blog") {

            get("") {
                val lst = if (call.request.queryString() == "") {
                    blogRepository.getAllBlog()
                } else {
                    blogRepository.getAllBlogByCategory(
                        category = call.parameters["category"].toString()
                    )
                }
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "", lst))

            }

            delete("/delete/{id}") {
                val parameters = try {
                    call.parameters["id"]!!.toInt()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 1", null))
                    return@delete
                }
                try {
                    val userId = call.principal<UserEntry>()!!.id
                    blogRepository.deleteBlog(parameters, userId)
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, "delete", null))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 2", null))
                }
            }

            post("/publish") {
                val parameters = try {
                    call.receive<CreateBlog>()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "Something happened1 !!", null))
                    return@post
                }

                try {
                    val userId = call.principal<UserEntry>()!!.id

                    val blog = blogRepository.createBlog(
                        userId = userId,
                        title = parameters.title,
                        content = parameters.content,
                        metaTitle = parameters.metaTitle,
                        summary = parameters.summary,
                        published = parameters.published,
                        createdAt = parameters.createdAt,
                        updatedAt = parameters.updatedAt,
                        publishedAt = parameters.publishedAt,
                        imgUrl = parameters.imgUrl,
                        category = parameters.category
                    )
                    if (blog == null) {
                        call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "Something happened2 !!", null))
                        return@post
                    } else {
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "blog created", null))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadGateway, SimpleResponse(false, "Something happened 3!!", null))
                }
            }

            get("/fav") {

                val lst = if (call.request.queryString() == "") {
                    try {
                        val userId = call.principal<UserEntry>()!!.id
                        val lst = blogRepository.getFavBlogs(userId)
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "success", lst))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 2", null))
                    }
                } else {
                    try {
                        val userId = call.principal<UserEntry>()!!.id
                        val lst = blogRepository.getFavBlogsByCategory(
                            userId = userId,
                            category = call.parameters["category"].toString()
                        )
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "success", lst))
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 2", null))
                    }
                }
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "", lst))
            }

            get("/{id}") {
                val parameters = try {
                    call.parameters["id"]!!.toInt()
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 1", null))
                    return@get
                }

                try {
                    val userId = try {
                        call.principal<UserEntry>()!!.id
                    } catch (t: Exception) {
                        call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${t.message} 4", null))
                        return@get
                    }
                    val response = blogRepository.getBlogById(parameters, userId)
                    if (response == null) {
                        call.respond(HttpStatusCode.ExpectationFailed, SimpleResponse(false, "not found", null))
                    } else {
                        call.respond(HttpStatusCode.OK, SimpleResponse(true, "", response))
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "${e.message} 2", null))
                }

            }

        }


    }
}