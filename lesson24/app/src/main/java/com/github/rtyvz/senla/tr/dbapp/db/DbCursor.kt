package com.github.rtyvz.senla.tr.dbapp.db

interface DbCursor {

    fun getLong(fieldName: String): Long?
    fun getString(fieldName: String): String
    fun fetch(): Boolean
    fun moveToFirst(): Boolean
    fun closeCursor()
}