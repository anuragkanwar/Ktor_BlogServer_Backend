package com.example.data.database

import io.ktor.server.auth.*

@kotlinx.serialization.Serializable
class UserEntry(
    val id: Int,
    val firstName: String,
    val middleName: String?,
    val lastName: String?,
    val mobile: String?,
    val email: String,
    val password: String,
    val intro: String,
    val profile: String,
    val imgUrl: String,
    val registerAt: Long,
    val lastLogin: Long
) : Principal