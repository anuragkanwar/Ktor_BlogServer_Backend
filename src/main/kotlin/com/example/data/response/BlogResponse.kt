package com.example.data.response

import com.example.data.database.CommentEntry

data class BlogResponse(
    val id: Int,
    val userid: Int,
    val title: String,
    val metaTitle: String,
    val category : String,
    val content: String,
    val summary: String,
    val published: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val publishedAt: Long,
    val likes: Long,
    val imgUrl: String,
    val bookmarked : Boolean,
    val comments: List<CommentEntry>,
    val authorName : String
)
