package com.github.rtyvz.senla.tr.dbapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentWithEmailEntity(
    val commentId: Long,
    val postId: Long,
    val email: String,
    val comment: String,
    val rate: Long
) : Parcelable