package com.example.model.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object FavBlogTable : IntIdTable() {
    val blogId: Column<Int> = integer("blogId").references(BlogTable.id)
    val userId: Column<Int> = integer("userId").references(UserTable.id)
}