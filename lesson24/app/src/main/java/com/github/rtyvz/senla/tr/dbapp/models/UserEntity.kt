package com.github.rtyvz.senla.tr.dbapp.models

data class UserEntity(
    val userId: Long = -1,
    val name: String,
    val lastName: String,
    val email: String
)