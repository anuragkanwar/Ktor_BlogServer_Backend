package com.example.data.database

import kotlinx.serialization.Serializable

@Serializable
data class CommentEntry(
    val id: Int,
    val blogId: Int,
    val UserId: Int,
    val content: String,
    val likes: Long,
    val publishedAt: Long
)
