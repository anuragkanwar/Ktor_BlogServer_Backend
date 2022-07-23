package com.example.data.requests

@kotlinx.serialization.Serializable
data class CreateBlog(
    val title: String,
    val content: String,
    val metaTitle: String,
    val summary: String,
    val published: Boolean,
    val createdAt: Long,
    val updatedAt: Long,
    val publishedAt: Long,
    val imgUrl: String,
    val category: String,
)
