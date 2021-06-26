package com.github.rtyvz.senla.tr.dbapp.db

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.github.rtyvz.senla.tr.dbapp.models.CommentEntity
import com.github.rtyvz.senla.tr.dbapp.models.PostAndUserEmailEntity
import com.github.rtyvz.senla.tr.dbapp.models.PostEntity
import com.github.rtyvz.senla.tr.dbapp.models.UserEntity

class DbHelper {

    companion object {
        private const val TAG = "TAG"
        private const val COLUMN_NAME_TITLE = "title"
        private const val COLUMN_NAME_EMAIL = "email"
        private const val COLUMN_NAME_BODY = "body"
    }

    fun insertUserData(db: SQLiteDatabase, data: List<UserEntity>) {
        data.forEach {
            InsertValueHelper().apply {
                table("user")
                fields("firstName, lastName, email")
                values("(?,?,?)")
                insert(db, listOf(it.name, it.lastName, it.email))
            }
        }
        db.close()
    }

    fun insertPostData(db: SQLiteDatabase, postList: List<PostEntity>) {
        postList.forEach {
            InsertValueHelper().apply {
                table("post")
                fields("userId, title, body, rate")
                values("(?,?,?,?)")
                insert(db, listOf(it.userId, it.title, it.body, it.rate))
            }
        }
        db.close()
    }

    fun insertCommentData(db: SQLiteDatabase, commentList: List<CommentEntity>) {
        commentList.forEach {
            InsertValueHelper().apply {
                table("comment")
                fields("postId, userId, text")
                values("(?,?,?)")
                insert(db, listOf(it.postId, it.userId, it.text))
            }
        }
        db.close()
    }


    fun getPostWithEmails(db: SQLiteDatabase): List<PostAndUserEmailEntity> {
        val listWithPostAndEmail = mutableListOf<PostAndUserEmailEntity>()

        val cursor = SelectDataHelper().apply {
            select("post.title, user.email, post.body")
            fromTables("post, user")
            where("post.UserId = user.id")
        }.select(db)
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        listWithPostAndEmail.add(
                            PostAndUserEmailEntity(
                                cursor.getString(COLUMN_NAME_TITLE),
                                cursor.getString(COLUMN_NAME_EMAIL),
                                cursor.getString(COLUMN_NAME_BODY)
                            )
                        )
                    } while (cursor.fetch())
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        } finally {
            cursor?.closeCursor()
            db.close()
        }
        return listWithPostAndEmail
    }
}