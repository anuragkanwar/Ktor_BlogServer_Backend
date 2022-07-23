package com.example.routes

import com.example.authentication.JwtService
import com.example.data.database.JwtRequirement
import com.example.data.database.UserEntry
import com.example.data.requests.LoginUser
import com.example.data.requests.RegisterUser
import com.example.data.requests.UpdateUser
import com.example.data.response.LoginResponse
import com.example.data.response.SimpleResponse
import com.example.repository.UserRepository
import com.example.utils.Constants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


const val USERS = "${Constants.API_VERSION}/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

fun Route.userRoutes(
    jwtService: JwtService, hashFunction: (String) -> String, userRepository: UserRepository
) {
    post("/v1/users/register") {
        val registerRequest = try {
            call.receive<RegisterUser>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "$ { e.message }", null))
            return@post
        }

        try {
            val user = userRepository.createUser(
                firstName = registerRequest.firstName,
                middleName = registerRequest.middleName,
                lastName = registerRequest.lastName,
                mobile = registerRequest.mobile,
                email = registerRequest.email,
                password = hashFunction(registerRequest.password),
                imageUrl = "",
                registerAt = registerRequest.registerAt,
                lastLogin = registerRequest.lastLogin,
            )
            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "some problem occurred", null))
            } else {
                val token = jwtService.generateToken(JwtRequirement(id = user.id))
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "authorized", LoginResponse(token, user)))
            }

        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, "$ { e.message }", null))
            return@post
        }
    }


    post("/v1/users/login") {

        val loginRequest = try {
            call.receive<LoginUser>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Credentials", null))
            return@post
        }

        try {
            val currUser = userRepository.getUserByEmail(loginRequest.email)

            if (currUser == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email", null))
                return@post
            }

            if (currUser.password == hashFunction(loginRequest.password)) {
                val token = jwtService.generateToken(JwtRequirement(id = currUser.id))
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "authorized", LoginResponse(token, currUser)))
            } else {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email", null))
                return@post
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "$ { e.message }", null))
            return@post
        }
    }

    authenticate("jwt") {

        put("v1/users/edit") {
            val parameters = try {
                call.receive<UpdateUser>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Error occurred, somethings wrong", null))
                return@put
            }

            try {
                val userId = call.principal<UserEntry>()!!.id
                userRepository.updateUser(
                    userId = userId,
                    firstName = parameters.firstName,
                    middleName = parameters.middleName,
                    lastName = parameters.lastName,
                    mobile = parameters.mobile,
                    email = parameters.email,
                    intro = parameters.intro,
                    profile = parameters.profile,
                    imageUrl = parameters.imageUrl
                )
                val user = userRepository.getUserByEmail(parameters.email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "", user))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(false, e.message ?: "Some Problem occurred.", null)
                )
            }
        }

        delete("/v1/users/delete") {
            try {
                val userId = call.principal<UserEntry>()!!.id
                userRepository.deleteUser(userId)
                call.respond(
                    HttpStatusCode.OK, SimpleResponse(true, "User Deleted", null)
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Some problem occurred", null))
            }
        }


    }


}