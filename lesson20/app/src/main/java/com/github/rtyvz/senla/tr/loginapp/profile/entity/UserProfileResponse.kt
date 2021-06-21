package com.github.rtyvz.senla.tr.loginapp.profile.entity

data class UserProfileResponse(
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Long,
    val notes: String
)