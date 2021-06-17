package com.github.rtyvz.senla.tr.loginapp

data class UserProfileResponse(
    val firstName: String,
    val lastName: String,
    val birthDate: Long,
    val notes: String
)