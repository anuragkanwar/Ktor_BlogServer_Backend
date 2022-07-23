package com.example.data.response

import com.example.data.database.UserEntry

data class LoginResponse(
    val token: String,
    val user: UserEntry
)
