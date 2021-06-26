package com.github.rtyvz.senla.tr.dbapp.db

import android.database.sqlite.SQLiteDatabase

class SelectDataHelper {
    private val listWithPartOfQuery = mutableListOf<String>()

    companion object {
        private const val SELECT_OPERATOR = "SELECT "
        private const val FROM_OPERATOR = " FROM "
        private const val WHERE_OPERATOR = " WHERE "
    }

    fun select(fields: String) {
        listWithPartOfQuery.add(SELECT_OPERATOR)
        listWithPartOfQuery.add(fields)
    }

    fun fromTables(tables: String) {
        listWithPartOfQuery.add(FROM_OPERATOR)
        listWithPartOfQuery.add(tables)
    }

    fun where(condition: String) {
        listWithPartOfQuery.add(WHERE_OPERATOR)
        listWithPartOfQuery.add(condition)
    }

    fun select(db: SQLiteDatabase): DbCursor? {
        return DbCursorImpl(
            db.rawQuery(
                listWithPartOfQuery.joinToString(
                    separator = "",
                    prefix = "",
                    postfix = ""
                ), null
            )
        )
    }
}