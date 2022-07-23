package com.example.repository

import com.example.data.database.UserEntry
import com.example.model.dao.UserTableDAO
import com.example.model.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class UserRepository : UserTableDAO {
    override suspend fun createUser(
        firstName: String,
        middleName: String,
        lastName: String,
        mobile: String,
        email: String,
        password: String,
        imageUrl: String,
        registerAt: Long,
        lastLogin: Long
    ): UserEntry? {

        val row = dbQuery {
            UserTable.insert { tbl ->
                tbl[UserTable.firstName] = firstName
                tbl[UserTable.middleName] = middleName
                tbl[UserTable.lastName] = lastName
                tbl[UserTable.mobile] = mobile
                tbl[UserTable.email] = email
                tbl[UserTable.password] = password
                tbl[UserTable.imgUrl] = imageUrl
                tbl[UserTable.registerAt] = registerAt
                tbl[UserTable.lastLogin] = lastLogin
                tbl[UserTable.intro] = ""
                tbl[UserTable.profile] = ""
            }
        }
        return rowToUser(row.resultedValues?.get(0))
    }

    override suspend fun deleteUser(userId: Int): Int {
        return dbQuery {
            UserTable.deleteWhere {
                UserTable.id.eq(userId)
            }
        }
    }

    override suspend fun updateUser(
        userId: Int,
        firstName: String,
        middleName: String,
        lastName: String,
        mobile: String,
        email: String,
        intro: String,
        profile: String,
        imageUrl: String
    ): Int {
        return dbQuery {
            UserTable.update({
                UserTable.id.eq(userId)
            }) { tbl ->
                tbl[UserTable.firstName] = firstName
                tbl[UserTable.middleName] = middleName
                tbl[UserTable.lastName] = lastName
                tbl[UserTable.mobile] = mobile
                tbl[UserTable.email] = email
                tbl[UserTable.imgUrl] = imageUrl
                tbl[UserTable.intro] = intro
                tbl[UserTable.profile] = profile
            }
        }
    }

    override suspend fun updateLoginTime(userId: Int, lastLogin: Long): Int {
        return dbQuery {
            UserTable.update({
                UserTable.id.eq(userId)
            }) { tbl ->
                tbl[UserTable.lastLogin] = lastLogin
            }
        }
    }

    override suspend fun getUserById(userId: Int): UserEntry? {
        return dbQuery {
            UserTable.select {
                UserTable.id.eq(userId)
            }.map {
                rowToUser(it)
            }.singleOrNull()
        }
    }

    override suspend fun getUserByEmail(email: String): UserEntry? {
        return dbQuery {
            UserTable.select {
                UserTable.email.eq(email)
            }.map {
                rowToUser(it)
            }.singleOrNull()
        }
    }

//    override suspend fun updatePassword(email: String): Int {
//
//    }


    private fun rowToUser(resultRow: ResultRow?): UserEntry? {
        return if (resultRow == null) {
            null
        } else {
            UserEntry(
                id = resultRow[UserTable.id].value,
                firstName = resultRow[UserTable.firstName],
                middleName = resultRow[UserTable.middleName],
                lastName = resultRow[UserTable.lastName],
                mobile = resultRow[UserTable.mobile],
                email = resultRow[UserTable.email],
                password = resultRow[UserTable.password],
                intro = resultRow[UserTable.intro],
                profile = resultRow[UserTable.profile],
                imgUrl = resultRow[UserTable.imgUrl],
                registerAt = resultRow[UserTable.registerAt],
                lastLogin = resultRow[UserTable.lastLogin]
            )
        }

    }
}