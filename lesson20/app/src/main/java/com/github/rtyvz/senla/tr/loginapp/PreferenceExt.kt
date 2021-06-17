package com.github.rtyvz.senla.tr.loginapp

import android.content.SharedPreferences

fun SharedPreferences.getString(key: String): String? {
    return this.getString(key, "")
}

fun SharedPreferences.putString(key: String, value: String) {
    this.edit().putString(key, value).apply()
}