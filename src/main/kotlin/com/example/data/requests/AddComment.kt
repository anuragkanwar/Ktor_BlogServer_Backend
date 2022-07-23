package com.example.data.requests

@kotlinx.serialization.Serializable
data class AddComment (
    val blogId: Int,
    val content: String,
    val publishedAt: Long
)