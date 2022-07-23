package com.example.model.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object CategoryTable : IntIdTable() {
    val category: Column<String> = varchar("category", 50)
}