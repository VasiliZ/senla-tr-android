package com.github.rtyvz.senla.tr.loginapp

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val responseBody: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()

}