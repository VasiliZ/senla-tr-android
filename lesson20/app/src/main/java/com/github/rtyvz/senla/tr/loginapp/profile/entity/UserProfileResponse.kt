package com.github.rtyvz.senla.tr.loginapp.profile.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfileResponse(
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Long,
    val notes: String
) : Parcelable