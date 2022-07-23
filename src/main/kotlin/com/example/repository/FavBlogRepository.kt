package com.example.repository

import com.example.data.database.FavBlogEntry
import com.example.model.dao.FavBlogTableDAO
import com.example.model.table.FavBlogTable
import com.example.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert

class FavBlogRepository : FavBlogTableDAO {
    override suspend fun addToFav(blogId: Int, userId: Int): FavBlogEntry? {
        val row = dbQuery {
            FavBlogTable.insert { tbl ->
                tbl[FavBlogTable.blogId] = blogId
                tbl[FavBlogTable.userId] = userId
            }
        }

        return rowToFav(row.resultedValues?.get(0))
    }

    override suspend fun removeFomFav(blogId: Int, userId: Int): Int {
        return dbQuery {
            FavBlogTable.deleteWhere {
                FavBlogTable.blogId.eq(blogId) and FavBlogTable.userId.eq(userId)
            }
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

}