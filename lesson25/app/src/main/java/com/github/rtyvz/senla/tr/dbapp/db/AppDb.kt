package com.github.rtyvz.senla.tr.dbapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.rtyvz.senla.tr.dbapp.models.*
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider

class AppDb(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "app.bd"
        private const val DB_VERSION = 2
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
        for (version in 1..DB_VERSION) {
            migrate(db, version)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        for (version in 1..DB_VERSION) {
            migrate(db, version)
        }
    }

    private fun migrate(db: SQLiteDatabase?, dbVersion: Int) {
        when (dbVersion) {
            1 -> createTables(db)
            2 -> mergeFirstAndLastUserName(db)
        }
    }

    private fun createTables(db: SQLiteDatabase?) {
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

    private fun mergeFirstAndLastUserName(db: SQLiteDatabase?) {
//        db?.execSQL("drop index user")

        CreateTableHelper().apply {
            setTableName("temp_user_table")
            setPkField(PRIMARY_FIELD)
            addField("userFullName", TEXT_NOT_NULL_TYPE_FIELD)
            addField(USER_EMAIL_FIELD, TEXT_NOT_NULL_TYPE_FIELD)
            createTable(db)
        }

        InsertValueHelper().apply {
            table("temp_user_table")
            fields("userFullName, email")
            select()
            selectFields("(firstName || ' ' || lastName) as fullName, email")
            from(TABLE_NAME_USER)
            insertFromSelect(db)
        }
    }

    inline fun <reified T> populateTable(listData: List<T>) {
        when (T::class) {
            UserEntity::class -> DbHelper().insertUserData(
                DbProvider.provideDb().writableDatabase,
                listData as List<UserEntity>
            )
            PostEntity::class -> DbHelper().insertPostData(
                DbProvider.provideDb().writableDatabase,
                listData as List<PostEntity>
            )
            CommentEntity::class -> DbHelper().insertCommentData(
                DbProvider.provideDb().writableDatabase,
                listData as List<CommentEntity>
            )
            else -> {
                throw IllegalArgumentException("Invalid class")
            }
        }
    }

    fun getPostWithEmails(): List<PostAndUserEmailEntity> {
        return DbHelper().getPostWithEmails(DbProvider.provideDb().writableDatabase)
    }

    fun getDetailPost(postId: Long): DetailPost? {
        return DbHelper().getDetailPost(DbProvider.provideDb().writableDatabase, postId)
    }

    fun getPostComments(postId: Long): List<CommentWithEmailEntity> {
        return DbHelper().getCommentsWithEmail(DbProvider.provideDb().writableDatabase, postId)
    }
}