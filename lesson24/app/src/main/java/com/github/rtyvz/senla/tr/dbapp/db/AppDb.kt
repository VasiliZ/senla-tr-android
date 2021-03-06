package com.github.rtyvz.senla.tr.dbapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.rtyvz.senla.tr.dbapp.App
import com.github.rtyvz.senla.tr.dbapp.models.*

class AppDb(context: Context) :
    SQLiteOpenHelper(context, App.DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_VERSION = 1
        private const val TABLE_NAME_POST = "post"
        private const val TABLE_NAME_COMMENT = "comment"
        private const val TABLE_NAME_USER = "user"
        private const val USER_ID_FIELD = "userId"
        private const val USER_FIRST_NAME_FIELD = "firstName"
        private const val USER_LAST_NAME_FIELD = "lastName"
        private const val USER_EMAIL_FIELD = "email"
        private const val TITLE_FIELD = "title"
        private const val BODY_FIELD = "body"
        private const val RATE_FIELD = "rate"
        private const val PRIMARY_FIELD = "id"
        private const val POST_ID_FIELD = "postId"
        private const val TEXT_FIELD = "text"
        private const val INT_NOT_NULL_TYPE_FIELD = "INTEGER NOT NULL"
        private const val TEXT_NOT_NULL_TYPE_FIELD = "TEXT NOT NULL"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        CreateTableHelper().apply {
            setTableName(TABLE_NAME_POST)
            setPkField(PRIMARY_FIELD)
            addField(USER_ID_FIELD, INT_NOT_NULL_TYPE_FIELD)
            addField(TITLE_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            addField(BODY_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            addField(RATE_FIELD, INT_NOT_NULL_TYPE_FIELD)
            createTable(db)
        }

        CreateTableHelper().apply {
            setTableName(TABLE_NAME_COMMENT)
            setPkField(PRIMARY_FIELD)
            addField(POST_ID_FIELD, INT_NOT_NULL_TYPE_FIELD)
            addField(USER_ID_FIELD, INT_NOT_NULL_TYPE_FIELD)
            addField(TEXT_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            createTable(db)
        }

        CreateTableHelper().apply {
            setTableName(TABLE_NAME_USER)
            setPkField(PRIMARY_FIELD)
            addField(USER_FIRST_NAME_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            addField(USER_LAST_NAME_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            addField(USER_EMAIL_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            createTable(db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    inline fun <reified T> populateTable(listData: List<T>) {
        when (T::class) {
            UserEntity::class -> DbHelper().insertUserData(
                this.writableDatabase,
                listData as List<UserEntity>
            )
            PostEntity::class -> DbHelper().insertPostData(
                this.writableDatabase,
                listData as List<PostEntity>
            )
            CommentEntity::class -> DbHelper().insertCommentData(
                this.writableDatabase,
                listData as List<CommentEntity>
            )
            else -> {
                throw IllegalArgumentException("Invalid class")
            }
        }
    }

    fun getPostWithEmails(): List<PostAndUserEmailEntity> {
        return DbHelper().getPostWithEmails(this.writableDatabase)
    }

    fun getDetailPost(postId: Long): DetailPost? {
        return DbHelper().getDetailPost(this.writableDatabase, postId)
    }

    fun getPostComments(postId: Long): List<CommentWithEmailEntity> {
        return DbHelper().getCommentsWithEmail(this.writableDatabase, postId)
    }
}