package com.example.repository

import com.example.data.database.BlogEntry
import com.example.data.database.CommentEntry
import com.example.data.database.FavBlogEntry
import com.example.data.response.BlogResponse
import com.example.data.response.BlogSimpleResponse
import com.example.model.dao.BlogTableDAO
import com.example.model.table.BlogCommentTable
import com.example.model.table.BlogTable
import com.example.model.table.FavBlogTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class BlogRepository : BlogTableDAO {
    override suspend fun createBlog(
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
    ): BlogEntry? {
        val row = dbQuery {
            BlogTable.insert { tbl ->
                tbl[BlogTable.userId] = userId
                tbl[BlogTable.title] = title
                tbl[BlogTable.content] = content
                tbl[BlogTable.category] = category
                tbl[BlogTable.metaTitle] = metaTitle
                tbl[BlogTable.summary] = summary
                tbl[BlogTable.published] = published
                tbl[BlogTable.createdAt] = createdAt
                tbl[BlogTable.updatedAt] = updatedAt
                tbl[BlogTable.publishedAt] = publishedAt
                tbl[imageUrl] = imgUrl
                tbl[likes] = 0
            }
        }
        return rowToBlog(row.resultedValues?.get(0))
    }

    override suspend fun updateBlog(
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
    ): Int {
        return dbQuery {
            BlogTable.update(
                {
                    BlogTable.id.eq(id)
                }
            ) { tbl ->
                tbl[BlogTable.title] = title
                tbl[BlogTable.content] = content
                tbl[BlogTable.metaTitle] = metaTitle
                tbl[BlogTable.summary] = summary
                tbl[BlogTable.published] = published
                tbl[BlogTable.createdAt] = createdAt
                tbl[BlogTable.updatedAt] = updatedAt
                tbl[BlogTable.category] = category
                tbl[imageUrl] = imgUrl
                tbl[BlogTable.publishedAt] = publishedAt
            }
        }
    }

    override suspend fun deleteBlog(id: Int, userId: Int): Int {
        return dbQuery {
            BlogTable.deleteWhere {
                BlogTable.id.eq(id) and BlogTable.userId.eq(userId)
            }
        }
    }

    override suspend fun getAllBlog(): List<BlogSimpleResponse> {
        return dbQuery {
            var rowEntries = BlogTable.selectAll().orderBy(BlogTable.likes to SortOrder.DESC).mapNotNull {
                rowToBlog(it)
            }

            val blogs = mutableListOf<BlogSimpleResponse>()

            rowEntries.forEach { blog ->
                val item = BlogSimpleResponse(
                    id = blog.id,
                    title = blog.title,
                    publishedAt = blog.publishedAt,
                    imgUrl = blog.imgUrl
                )
                blogs.add(item)
            }
            blogs
        }
    }

    override suspend fun getAllBlogByCategory(category: String): List<BlogSimpleResponse> {
        return dbQuery {
            val rowEntries = BlogTable.select {
                BlogTable.category.eq(category)
            }.orderBy(BlogTable.likes to SortOrder.DESC).mapNotNull {
                rowToBlog(it)
            }

            val blogs = mutableListOf<BlogSimpleResponse>()

            rowEntries.forEach { blog ->
                val item = BlogSimpleResponse(
                    id = blog.id,
                    title = blog.title,
                    publishedAt = blog.publishedAt,
                    imgUrl = blog.imgUrl
                )
                blogs.add(item)
            }
            blogs
        }
    }

    override suspend fun getBlogById(id: Int, userId: Int): BlogResponse? {
        return dbQuery {
            val row = BlogTable.select {
                BlogTable.id.eq(id)
            }.map {
                rowToBlog(it)
            }.singleOrNull()

            val bkmrkd = FavBlogTable.select {
                FavBlogTable.blogId.eq(id) and FavBlogTable.userId.eq(userId)
            }.map {
                rowToFav(it)
            }.singleOrNull()


            val cmnts = BlogCommentTable.select {
                BlogCommentTable.blogId.eq(row!!.id)
            }.orderBy(BlogCommentTable.publishedAt to SortOrder.ASC).map {
                rowToComment(it)
            }.filterNotNull()

            BlogResponse(
                id = row!!.id,
                userid = row.userid,
                title = row.title,
                metaTitle = row.metaTitle,
                category = row.category,
                content = row.content,
                summary = row.summary,
                published = row.published,
                createdAt = row.createdAt,
                updatedAt = row.updatedAt,
                publishedAt = row.publishedAt,
                likes = row.likes,
                imgUrl = row.imgUrl,
                bookmarked = (bkmrkd != null),
                comments = cmnts,
            )

        }
    }

    override suspend fun getFavBlogs(userId: Int): List<BlogSimpleResponse> {
        return dbQuery {
            val rowEntries = BlogTable.select {
                BlogTable.id inSubQuery (
                        FavBlogTable.slice(FavBlogTable.blogId).select {
                            FavBlogTable.userId.eq(userId)
                        })
            }.orderBy(BlogTable.likes to SortOrder.DESC).mapNotNull {
                rowToBlog(it)
            }

            val blogs = mutableListOf<BlogSimpleResponse>()

            rowEntries.forEach { blog ->
                val item = BlogSimpleResponse(
                    id = blog.id,
                    title = blog.title,
                    publishedAt = blog.publishedAt,
                    imgUrl = blog.imgUrl
                )
                blogs.add(item)
            }
            blogs
        }
    }

    override suspend fun getFavBlogsByCategory(userId: Int, category: String): List<BlogSimpleResponse> {
        return dbQuery {
            val rowEntries = BlogTable.select {
                BlogTable.id inSubQuery (
                        FavBlogTable.slice(FavBlogTable.blogId).select {
                            FavBlogTable.userId.eq(userId)
                        }) and BlogTable.category.eq(category)
            }.orderBy(BlogTable.likes to SortOrder.DESC).mapNotNull {
                rowToBlog(it)
            }

            val blogs = mutableListOf<BlogSimpleResponse>()

            rowEntries.forEach { blog ->
                val item = BlogSimpleResponse(
                    id = blog.id,
                    title = blog.title,
                    publishedAt = blog.publishedAt,
                    imgUrl = blog.imgUrl
                )
                blogs.add(item)
            }
            blogs
        }
    }


    private fun rowToFav(resultRow: ResultRow?): FavBlogEntry? {
        return if (resultRow == null) {
            null
        } else {
            FavBlogEntry(
                blogId = resultRow[FavBlogTable.blogId],
                userId = resultRow[FavBlogTable.userId]
            )
        }
    }


    private fun rowToComment(resultRow: ResultRow?): CommentEntry? {
        return if (resultRow == null) {
            null
        } else {
            CommentEntry(
                id = resultRow[BlogCommentTable.id].value,
                blogId = resultRow[BlogCommentTable.blogId],
                UserId = resultRow[BlogCommentTable.userId],
                content = resultRow[BlogCommentTable.content],
                likes = resultRow[BlogCommentTable.likes],
                publishedAt = resultRow[BlogCommentTable.publishedAt]
            )
        }
    }


    private fun rowToBlog(resultRow: ResultRow?): BlogEntry? {
        return if (resultRow == null) {
            null
        } else {
            BlogEntry(
                id = resultRow[BlogTable.id].value,
                userid = resultRow[BlogTable.userId],
                title = resultRow[BlogTable.title],
                metaTitle = resultRow[BlogTable.metaTitle],
                category = resultRow[BlogTable.category],
                content = resultRow[BlogTable.content],
                summary = resultRow[BlogTable.summary],
                published = resultRow[BlogTable.published],
                createdAt = resultRow[BlogTable.createdAt],
                updatedAt = resultRow[BlogTable.updatedAt],
                publishedAt = resultRow[BlogTable.publishedAt],
                likes = resultRow[BlogTable.likes],
                imgUrl = resultRow[BlogTable.imageUrl],
            )
        }
    }


}