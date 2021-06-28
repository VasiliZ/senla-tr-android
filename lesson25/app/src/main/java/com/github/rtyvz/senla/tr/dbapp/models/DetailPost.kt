package com.github.rtyvz.senla.tr.dbapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailPost(
    val postId: Long,
    val postTitle: String,
    val email: String,
    val authorFullName: String,
    val postBody: String
) : Parcelable