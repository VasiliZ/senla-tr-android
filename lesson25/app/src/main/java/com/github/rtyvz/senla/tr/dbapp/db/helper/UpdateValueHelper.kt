package com.github.rtyvz.senla.tr.dbapp.db.helper

import android.database.sqlite.SQLiteDatabase

class UpdateValueHelper {
    private val listWithPartsOfQuery = mutableListOf<String>()

    companion object {
        private const val UPDATE_OPERATOR = " UPDATE "
        private const val SET_OPERATOR = " SET "
        private const val WHERE_OPERATOR = " WHERE "
        private const val EMPTY_STRING = ""
    }

    fun update(table: String) {
        listWithPartsOfQuery.add(UPDATE_OPERATOR)
        listWithPartsOfQuery.add(table)
    }

    fun set() {
        listWithPartsOfQuery.add(SET_OPERATOR)
    }

    fun values(fieldsForChange: String) {
        listWithPartsOfQuery.add(fieldsForChange)
    }

    fun where() {
        listWithPartsOfQuery.add(WHERE_OPERATOR)
    }

    fun condition(condition: String) {
        listWithPartsOfQuery.add(condition)
    }

    fun insert(db: SQLiteDatabase?) {
        db?.execSQL(
            listWithPartsOfQuery.joinToString(
                separator = EMPTY_STRING,
                postfix = EMPTY_STRING,
                prefix = EMPTY_STRING
            )
        )
    }
}