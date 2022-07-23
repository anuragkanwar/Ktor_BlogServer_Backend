package com.example.model.dao

import com.example.data.database.CommentEntry
import com.example.data.requests.AddComment

interface BlogCommentTableDAO {

    suspend fun addComment(
        comment: AddComment, userId: Int
    ): CommentEntry?

    suspend fun editComment(
        comment: CommentEntry, userId: Int
    ): Int

    suspend fun deleteComment(
        id: Int, blogId: Int, userID: Int
    ): Int
}