package com.example.model.dao

import com.example.data.database.JwtRequirement
import com.example.data.database.UserEntry

interface UserTableDAO {

    suspend fun createUser(
        firstName: String,
        middleName: String,
        lastName: String,
        mobile: String,
        email: String,
        password: String,
        imageUrl: String,
        registerAt: Long,
        lastLogin: Long
    ): UserEntry?

    suspend fun deleteUser(
        userId: Int,
    ): Int

    suspend fun updateUser(
        userId: Int,
        firstName: String,
        middleName: String,
        lastName: String,
        mobile: String,
        email: String,
        intro: String,
        profile: String,
        imageUrl: String,
    ): Int

    suspend fun updateLoginTime(
        userId: Int,
        lastLogin: Long
    ): Int

    suspend fun getUserById(
        userId: Int
    ): UserEntry?

    suspend fun getUserByEmail(
        email: String
    ): UserEntry?

//    suspend fun updatePassword(
//        email: String,
//    ): Int
}