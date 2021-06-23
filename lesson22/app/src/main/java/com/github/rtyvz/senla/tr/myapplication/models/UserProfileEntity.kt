package com.github.rtyvz.senla.tr.myapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfileEntity(
    val userEmail: String? = null,
    val firstUserName: String,
    val lastUserName: String,
    val birthDate: String,
    val userNotes: String,
    val message: String? = null
) : Parcelable