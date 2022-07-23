package com.example.plugins

import com.example.authentication.JwtService
import com.example.data.response.SimpleResponse
import com.example.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlin.collections.set

fun Application.configureSecurity() {

    val userRepository = UserRepository()
    val jwt = JwtService()

    authentication {
        jwt("jwt") {
            verifier(jwt.verifier)
            realm = "Blog Server"
            validate {
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimInt = claim.asInt()
                val user = userRepository.getUserById(claimInt)
                user
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, SimpleResponse(false, "Token is not valid or has expired", null))
            }

        }
    }
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
