package com.github.rtyvz.senla.tr.loginapp.login.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserTokenResponse(
    val message: String,
    val token: String
):Parcelable