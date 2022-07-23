package com.example.data.response

import com.example.data.database.CommentEntry

data class BlogSimpleResponse(
    val id: Int,
    val title: String,
    val publishedAt: Long,
    val imgUrl: String,
)
