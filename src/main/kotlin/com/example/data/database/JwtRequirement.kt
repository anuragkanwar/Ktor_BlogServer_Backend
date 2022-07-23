package com.example.data.database

import io.ktor.server.auth.*

data class JwtRequirement(
    val id: Int
) : Principal
