package com.example

import com.example.authentication.JwtService
import com.example.authentication.hash
import io.ktor.server.application.*
import com.example.plugins.*
import com.example.repository.DatabaseFactory

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
}
