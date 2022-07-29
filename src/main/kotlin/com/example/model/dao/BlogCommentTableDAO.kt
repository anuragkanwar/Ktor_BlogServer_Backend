package com.example.model.dao

import com.example.data.database.CommentEntry
import com.example.data.requests.AddComment
import com.example.data.response.CommentResponse

interface BlogCommentTableDAO {

    suspend fun getComments(
        blogId: Int
    ) : List<CommentResponse?>

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