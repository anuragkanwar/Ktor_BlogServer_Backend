package com.example.model.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object BlogTable : IntIdTable() {
    val userId: Column<Int> = integer("userId").references(UserTable.id)
    val title: Column<String> = varchar("title", 512)
    val content: Column<String> = text("content")
    val category: Column<String> = varchar("category", 50)
    val metaTitle: Column<String> = varchar("metaTitle", 512)
    val summary: Column<String> = varchar("summary", 512)
    val published: Column<Boolean> = bool("published")
    val createdAt: Column<Long> = long("createdAt")
    val updatedAt: Column<Long> = long("updatedAt")
    val publishedAt: Column<Long> = long("publishedAt")
    val likes: Column<Long> = long("likes").default(0)
    val imageUrl: Column<String> = varchar("imageUrl", 512)
}