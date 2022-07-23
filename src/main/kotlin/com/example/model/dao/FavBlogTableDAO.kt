package com.example.model.dao

import com.example.data.database.FavBlogEntry

interface FavBlogTableDAO {

    suspend fun addToFav(
        blogId: Int, userId: Int
    ): FavBlogEntry?

    suspend fun removeFomFav(
        blogId: Int, userId: Int
    ): Int
}