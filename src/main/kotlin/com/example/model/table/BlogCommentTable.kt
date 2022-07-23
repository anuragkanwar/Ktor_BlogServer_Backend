package com.example.model.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.Locale

object BlogCommentTable : IntIdTable(){
    val blogId : Column<Int> = integer("blogId").references(BlogTable.id)
    val userId : Column<Int> = integer("userId").references(UserTable.id)
    val content : Column<String> = text("content")
    val publishedAt : Column<Long> = long("publishedAt")
    val likes : Column<Long> = long("likes").default(0)
}