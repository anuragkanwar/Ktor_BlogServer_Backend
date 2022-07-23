package com.example.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.database.JwtRequirement
import java.util.*

class JwtService {
    private val issuer = "blogServer"
    private val jwtSecret = System.getenv("JWT_SECRET")
    private val algorithm = Algorithm.HMAC512(jwtSecret)


    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()

    fun generateToken(user: JwtRequirement): String {
        return JWT.create().withSubject("ServiceAuthentication").withIssuer(issuer).withClaim("id", user.id)
            .withExpiresAt(expireToken())
            .sign(algorithm)
    }

    private fun expireToken() = Date(System.currentTimeMillis() + 36_00_00 * 24)

}