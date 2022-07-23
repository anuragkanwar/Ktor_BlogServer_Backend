package com.example.data.requests

import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource("/v1/users/login")
data class LoginUser(
    val email: String,
    val password: String
)
