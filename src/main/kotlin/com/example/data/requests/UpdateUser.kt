package com.example.data.requests

@kotlinx.serialization.Serializable
data class UpdateUser(
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val mobile: String,
    val email: String,
    val intro: String,
    val profile: String,
    val imageUrl: String
)
