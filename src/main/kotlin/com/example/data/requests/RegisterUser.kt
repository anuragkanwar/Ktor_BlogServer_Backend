package com.example.data.requests

import com.example.routes.REGISTER_REQUEST
import io.ktor.resources.*

@kotlinx.serialization.Serializable
@Resource(REGISTER_REQUEST)
data class RegisterUser(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val mobile: String,
    val email: String,
    val password: String,
    val registerAt: Long,
    val lastLogin: Long
)
