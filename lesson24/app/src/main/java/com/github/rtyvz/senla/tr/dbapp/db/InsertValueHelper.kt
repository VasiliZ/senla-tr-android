package com.github.rtyvz.senla.tr.dbapp.db

import android.database.sqlite.SQLiteDatabase

class InsertValueHelper {
    private val listWithPartsOfQuery = mutableListOf<String>()

    companion object {
        private const val INSERT_OPERATION = "insert into "
        private const val OPEN_BRACKET = "("
        private const val CLOSE_BRACKET = ")"
        private const val VALUES = "VALUES"
        private const val FIRST_INDEX_OF_STATEMENT = 1
        private const val EMPTY_STRING = ""
    }

    fun table(tableName: String) {
        listWithPartsOfQuery.add(INSERT_OPERATION)
        listWithPartsOfQuery.add(tableName)
    }

    fun fields(fieldsName: String) {
        listWithPartsOfQuery.add(OPEN_BRACKET)
        listWithPartsOfQuery.add(fieldsName)
        listWithPartsOfQuery.add(CLOSE_BRACKET)
    }

    fun values(values: String) {
        listWithPartsOfQuery.add(VALUES)
        listWithPartsOfQuery.add(values)
    }

    fun insert(db: SQLiteDatabase, listArgs: List<Any>) {
        val statement = db.compileStatement(
            listWithPartsOfQuery.joinToString(
                separator = EMPTY_STRING,
                prefix = EMPTY_STRING,
                postfix = EMPTY_STRING
            )
        )
        listArgs.forEachIndexed { index, value ->
            when (value) {
                is String -> statement.bindString(
                    index + FIRST_INDEX_OF_STATEMENT,
                    value.toString()
                )
                is Int -> statement.bindLong(index + FIRST_INDEX_OF_STATEMENT, value.toLong())
                is Long -> statement.bindLong(index + FIRST_INDEX_OF_STATEMENT, value.toLong())
                is Double -> statement.bindDouble(
                    index + FIRST_INDEX_OF_STATEMENT,
                    value.toDouble()
                )
                else -> statement.bindNull(index + FIRST_INDEX_OF_STATEMENT)
            }
        }
        statement.executeInsert()
    }
}