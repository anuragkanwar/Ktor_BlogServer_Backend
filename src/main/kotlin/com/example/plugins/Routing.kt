package com.example.plugins

import com.example.authentication.JwtService
import com.example.authentication.hash
import com.example.repository.BlogRepository
import com.example.repository.CommentRepository
import com.example.repository.FavBlogRepository
import com.example.repository.UserRepository
import com.example.routes.blogRoutes
import com.example.routes.commentRoutes
import com.example.routes.favBlogRoutes
import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }
    val userRepository = UserRepository()
    val blogRepository = BlogRepository()
    val favBlogRepository = FavBlogRepository()
    val commentRepository = CommentRepository()
    install(Resources)
    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        userRoutes(jwtService, hashFunction, userRepository)
        blogRoutes(blogRepository)
        favBlogRoutes(favBlogRepository)
        commentRoutes(commentRepository)
    }
}
