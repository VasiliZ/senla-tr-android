package com.github.rtyvz.senla.tr.dbapp.db

import android.database.sqlite.SQLiteDatabase

class SelectDataHelper {
    private val listWithPartOfQuery = mutableListOf<String>()

    companion object {
        private const val SELECT_OPERATOR = "SELECT "
        private const val FROM_OPERATOR = " FROM "
        private const val WHERE_OPERATOR = " WHERE "
        private const val AND_OPERATOR = " AND "
        private const val EMPTY_STRING = ""
    }

    fun select(fields: String) {
        listWithPartOfQuery.add(SELECT_OPERATOR)
        listWithPartOfQuery.add(fields)
    }

    fun fromTables(tables: String) {
        listWithPartOfQuery.add(FROM_OPERATOR)
        listWithPartOfQuery.add(tables)
    }

    fun where() {
        listWithPartOfQuery.add(WHERE_OPERATOR)
    }

    fun and() {
        listWithPartOfQuery.add(AND_OPERATOR)
    }

    fun condition(condition: String) {
        listWithPartOfQuery.add(condition)
    }

    fun select(db: SQLiteDatabase): DbCursor? {
        return DbCursorImpl(
            db.rawQuery(
                listWithPartOfQuery.joinToString(
                    separator = EMPTY_STRING,
                    prefix = EMPTY_STRING,
                    postfix = EMPTY_STRING
                ), null
            )
        )
    }
}