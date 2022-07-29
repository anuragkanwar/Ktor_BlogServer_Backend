package com.example.data.response

import kotlinx.serialization.Serializable

@Serializable
data class CommentResponse(
    val id: Int,
    val blogId: Int,
    val UserId: Int,
    val UserName: String,
    val UserImage: String,
    val content: String,
    val likes: Long,
    val publishedAt: Long
)