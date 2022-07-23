package com.example.model.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import java.util.Date

object UserTable : IntIdTable() {
    val firstName: Column<String> = varchar("firstName", 512)
    val middleName: Column<String> = varchar("middleName", 512)
    val lastName: Column<String> = varchar("lastName", 512)
    val mobile: Column<String> = varchar("mobile", 20)
    val email: Column<String> = varchar("email", 512).uniqueIndex()
    val password: Column<String> = varchar("password", 100)
    val registerAt: Column<Long> = long("registerAt")
    val lastLogin: Column<Long> = long("lastLogin")
    val intro: Column<String> = varchar("intro", 512)
    val profile: Column<String> = varchar("profile", 512)
    val imgUrl : Column<String> = varchar("imgUrl", 512)
}