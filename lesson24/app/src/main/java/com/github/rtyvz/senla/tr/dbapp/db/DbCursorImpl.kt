package com.github.rtyvz.senla.tr.dbapp.db

import android.database.Cursor

class DbCursorImpl(private val cursor: Cursor) : DbCursor {

    override fun getLong(fieldName: String): Long {
        val index = cursor.getColumnIndex(fieldName)
        return cursor.getLong(index)
    }

    override fun getString(fieldName: String): String {
        val index = cursor.getColumnIndex(fieldName)
        return cursor.getString(index)
    }

    override fun next(): Boolean {
        return cursor.moveToNext()
    }

    override fun moveToFirst(): Boolean {
        return cursor.moveToFirst()
    }

    override fun closeCursor() {
        cursor.close()
    }
}