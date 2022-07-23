package com.example.model.dao

import com.example.data.database.BlogEntry
import com.example.data.response.BlogResponse
import com.example.data.response.BlogSimpleResponse

interface BlogTableDAO {

    suspend fun createBlog(
        userId: Int,
        title: String,
        content: String,
        metaTitle: String,
        summary: String,
        published: Boolean,
        createdAt: Long,
        updatedAt: Long,
        publishedAt: Long,
        imgUrl: String,
        category: String
    ): BlogEntry?

    suspend fun updateBlog(
        id: Int,
        title: String,
        content: String,
        metaTitle: String,
        summary: String,
        published: Boolean,
        createdAt: Long,
        updatedAt: Long,
        publishedAt: Long,
        imgUrl: String,
        category: String
    ): Int

    suspend fun deleteBlog(id: Int, userId: Int): Int

    suspend fun getAllBlog(): List<BlogSimpleResponse>

    suspend fun getAllBlogByCategory(category: String): List<BlogSimpleResponse>

    suspend fun getBlogById(id: Int, userId: Int): BlogResponse?

    suspend fun getFavBlogs(userId: Int): List<BlogSimpleResponse>

    suspend fun getFavBlogsByCategory(userId: Int, category: String): List<BlogSimpleResponse>
}