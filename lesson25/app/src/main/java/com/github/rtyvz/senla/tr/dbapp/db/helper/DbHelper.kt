package com.github.rtyvz.senla.tr.dbapp.db.helper

import android.database.sqlite.SQLiteDatabase
import com.github.rtyvz.senla.tr.dbapp.models.*

class DbHelper {

    companion object {
        private const val COLUMN_NAME_TITLE = "title"
        private const val COLUMN_NAME_EMAIL = "email"
        private const val COLUMN_NAME_BODY = "body"
        private const val COLUMN_NAME_ID = "id"
        private const val COLUMN_NAME_FULL_USER_NAME = "userFullName"
        private const val COLUMN_NAME_TEXT = "text"
        private const val COLUMN_NAME_RATE = "rate"
        private const val COLUMN_NAME_POST_ID = "postId"
    }

    fun insertUserData(db: SQLiteDatabase?, data: List<UserEntity>) {
        data.forEach {
            InsertValueHelper().apply {
                table("user")
                fields("firstName, lastName, email")
                values("(?,?,?)")
                insert(db, listOf(it.name, it.lastName, it.email))
            }
        }
    }

    fun insertPostData(db: SQLiteDatabase?, postList: List<PostEntity>) {
        postList.forEach {
            InsertValueHelper().apply {
                table("post")
                fields("userId, title, body, rate")
                values("(?,?,?,?)")
                insert(db, listOf(it.userId, it.title, it.body, it.rate))
            }
        }

    }

    fun insertCommentData(db: SQLiteDatabase?, commentList: List<CommentEntity>) {
        commentList.forEach {
            InsertValueHelper().apply {
                table("comment")
                fields("postId, userId, text")
                values("(?,?,?)")
                insert(db, listOf(it.postId, it.userId, it.text))
            }
        }
    }


    fun getPostWithEmails(db: SQLiteDatabase): List<PostAndUserEmailEntity> {
        val listWithPostAndEmail = mutableListOf<PostAndUserEmailEntity>()

        val cursor = SelectDataHelper()
            .apply {
                select("post.id, post.title, user.email, post.body")
                fromTables("post, user")
                where()
                condition("post.UserId = user.id")
                orderBy("post.title")
                asc()
            }.select(db)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    listWithPostAndEmail.add(
                        PostAndUserEmailEntity(
                            cursor.getLong(COLUMN_NAME_ID),
                            cursor.getString(COLUMN_NAME_TITLE),
                            cursor.getString(COLUMN_NAME_EMAIL),
                            cursor.getString(COLUMN_NAME_BODY)
                        )
                    )
                } while (cursor.next())
            }
        }
        cursor?.closeCursor()
        db.close()

        return listWithPostAndEmail
    }

    fun getDetailPost(database: SQLiteDatabase, postId: Long): DetailPost? {
        var detailPost: DetailPost? = null
        val cursor = SelectDataHelper()
            .apply {
                select("post.id, post.title, user.email, user.$COLUMN_NAME_FULL_USER_NAME, post.body")
                fromTables("post, user")
                where()
                condition("post.userId = user.id")
                and()
                condition("post.id = $postId")
            }.select(database)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    detailPost = DetailPost(
                        cursor.getLong(COLUMN_NAME_ID),
                        cursor.getString(COLUMN_NAME_TITLE),
                        cursor.getString(COLUMN_NAME_EMAIL),
                        cursor.getString(COLUMN_NAME_FULL_USER_NAME),
                        cursor.getString(COLUMN_NAME_BODY)
                    )
                } while (cursor.next())
            }
        }
        cursor?.closeCursor()
        database.close()
        return detailPost
    }

    fun getCommentsWithEmail(database: SQLiteDatabase, postId: Long): List<CommentWithEmailEntity> {
        val listCommentWithEmail = mutableListOf<CommentWithEmailEntity>()

        val cursor = SelectDataHelper()
            .apply {
                select("comment.id, comment.postId, user.email, comment.text, comment.rate ")
                fromTables("user, comment, post")
                where()
                condition("post.id = comment.postId")
                and()
                condition("user.id = comment.userId")
                and()
                condition("post.id = $postId")
                orderBy("comment.id")
                asc()
            }.select(database)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    listCommentWithEmail.add(
                        CommentWithEmailEntity(
                            cursor.getLong(COLUMN_NAME_ID),
                            cursor.getLong(COLUMN_NAME_POST_ID),
                            cursor.getString(COLUMN_NAME_EMAIL),
                            cursor.getString(COLUMN_NAME_TEXT),
                            cursor.getLong(COLUMN_NAME_RATE)
                        )
                    )
                } while (cursor.next())
            }
        }
        cursor?.closeCursor()
        database.close()

        return listCommentWithEmail
    }

    fun changeCommentRate(value: String, commentId: Long, db: SQLiteDatabase?) {
        UpdateValueHelper().apply {
            update("comment")
            set()
            values("rate = rate $value")
            where()
            condition("comment.id = $commentId")
        }.insert(db)
    }

    fun getStatistics(db: SQLiteDatabase): List<StatisticsEntity> {
        val listWithStatisticsData = mutableListOf<StatisticsEntity>()
        val cursor = SelectDataHelper()
            .apply {
                select("avg(comment.rate) as avgRate, count(comment.id) as commentCount")
                fromTables("comment")
                groupBy("comment.postId")
                orderBy("commentCount")
                desc()
            }.select(db)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    listWithStatisticsData.add(
                        StatisticsEntity(
                            cursor.getDouble("avgRate"),
                            cursor.getLong("commentCount")
                        )
                    )
                } while (cursor.next())
            }
        }
        cursor?.closeCursor()

        return listWithStatisticsData
    }
}