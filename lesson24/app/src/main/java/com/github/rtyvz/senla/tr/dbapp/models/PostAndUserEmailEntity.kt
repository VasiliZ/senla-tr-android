package com.github.rtyvz.senla.tr.dbapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostAndUserEmailEntity(
    val titlePost: String,
    val userEmail: String,
    val postBody: String
) : Parcelable