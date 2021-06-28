package com.github.rtyvz.senla.tr.dbapp.models

data class CommentEntity(
    val id: Long = -1,
    val postId: Long,
    val userId: Long,
    val text: String
)