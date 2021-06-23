package com.github.rtyvz.senla.tr.myapplication.utils

import android.content.SharedPreferences

fun SharedPreferences.getString(key: String): String? {
    return this.getString(key, "")
}

fun SharedPreferences.putString(key: String, value: String) {
    this.edit().putString(key, value).apply()
}

fun SharedPreferences.clearProfilePrefs() {
    this.edit().clear().apply()
}