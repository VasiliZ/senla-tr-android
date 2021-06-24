package com.github.rtyvz.senla.tr.loginapp.utils

import android.content.SharedPreferences

private const val EMPTY_STRING = ""

fun SharedPreferences.getString(key: String): String? {
    return this.getString(key, EMPTY_STRING)
}

fun SharedPreferences.putString(key: String, value: String) {
    this.edit().putString(key, value).apply()
}

fun SharedPreferences.clearPrefs() {
    this.edit().clear().apply()
}