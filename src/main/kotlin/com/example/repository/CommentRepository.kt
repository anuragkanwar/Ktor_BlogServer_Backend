package com.example.repository

import com.example.data.database.CommentEntry
import com.example.data.requests.AddComment
import com.example.data.response.CommentResponse
import com.example.model.dao.BlogCommentTableDAO
import com.example.model.table.BlogCommentTable
import com.example.model.table.UserTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class CommentRepository : BlogCommentTableDAO {
    override suspend fun getComments(blogId: Int): List<CommentResponse?> {
        val row = dbQuery {
//            BlogCommentTable.select {
//                BlogCommentTable.blogId.eq(blogId)
//            }.mapNotNull {
//                rowToCommentResponse(it, "", "")
//            }

            BlogCommentTable.innerJoin(UserTable).slice(
                BlogCommentTable.id,
                BlogCommentTable.content,
                BlogCommentTable.publishedAt,
                BlogCommentTable.blogId,
                BlogCommentTable.userId,
                BlogCommentTable.likes,
                UserTable.imgUrl,
                UserTable.firstName,
                UserTable.lastName
            ).select {
                BlogCommentTable.blogId.eq(blogId) and UserTable.id.eq(BlogCommentTable.userId)
            }.map {
                rowToCommentResponse(it)
            }
        }
        return row
    }

    override suspend fun addComment(comment: AddComment, userId: Int): CommentEntry? {
        val row = dbQuery {
            BlogCommentTable.insert { tbl ->
                tbl[BlogCommentTable.blogId] = comment.blogId
                tbl[BlogCommentTable.publishedAt] = comment.publishedAt
                tbl[BlogCommentTable.userId] = userId
                tbl[BlogCommentTable.content] = comment.content
                tbl[BlogCommentTable.likes] = 0
            }
        }
        return rowToComment(row.resultedValues?.get(0))
    }

    override suspend fun editComment(comment: CommentEntry, userId: Int): Int {
        return dbQuery {
            BlogCommentTable.update({
                BlogCommentTable.id.eq(comment.id) and BlogCommentTable.userId.eq(userId)
            }) { tbl ->
                tbl[BlogCommentTable.blogId] = comment.blogId
                tbl[BlogCommentTable.likes] = comment.likes
                tbl[BlogCommentTable.publishedAt] = comment.publishedAt
                tbl[BlogCommentTable.content] = comment.content
            }
        }
    }

    override suspend fun deleteComment(id: Int, blogId: Int, userID: Int): Int {
        return dbQuery {
            BlogCommentTable.deleteWhere {
                BlogCommentTable.id.eq(id) and BlogCommentTable.userId.eq(userID) and BlogCommentTable.blogId.eq(blogId)
            }
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

    private fun rowToCommentResponse(resultRow: ResultRow?): CommentResponse? {
        return if (resultRow == null) {
            null
        } else {
            CommentResponse(
                id = resultRow[BlogCommentTable.id].value,
                blogId = resultRow[BlogCommentTable.blogId],
                UserId = resultRow[BlogCommentTable.userId],
                content = resultRow[BlogCommentTable.content],
                likes = resultRow[BlogCommentTable.likes],
                publishedAt = resultRow[BlogCommentTable.publishedAt],
                UserImage = resultRow[UserTable.imgUrl],
                UserName = resultRow[UserTable.firstName] + resultRow[UserTable.lastName]
            )
        }
    }


}