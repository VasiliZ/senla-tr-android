package com.github.rtyvz.senla.tr.dbapp.db

import android.database.sqlite.SQLiteDatabase

class SelectDataHelper {
    private val listWithPartOfQuery = mutableListOf<String>()

    companion object {
        private const val SELECT_OPERATOR = "SELECT "
        private const val FROM_OPERATOR = " FROM "
        private const val WHERE_OPERATOR = " WHERE "
        private const val AND_OPERATOR = " AND "
        private const val ORDER_BY_OPERATOR = " ORDER BY "
        private const val GROUP_BY_OPERATOR = " GROUP BY "
        private const val DESC_OPERATOR = " DESC "
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

    fun groupBy(columnName: String){
        listWithPartOfQuery.add(GROUP_BY_OPERATOR)
        listWithPartOfQuery.add(columnName)
    }

    fun orderBy(columnName:String){
        listWithPartOfQuery.add(ORDER_BY_OPERATOR)
        listWithPartOfQuery.add(columnName)
    }

    fun desc(){
        listWithPartOfQuery.add(DESC_OPERATOR)
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