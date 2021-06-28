package com.github.rtyvz.senla.tr.dbapp.models

data class PostEntity(
    val id: Long = -1,
    val userId: Long,
    val title: String,
    val body: String,
    val rate: Long
)