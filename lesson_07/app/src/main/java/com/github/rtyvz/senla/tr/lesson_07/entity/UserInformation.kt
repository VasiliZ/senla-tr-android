package com.github.rtyvz.senla.tr.lesson_07.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInformation(
    val login: String,
    val password: String,
    val name: String? = "",
    val secondName: String? = "",
    val sex: String? = "",
    val additionalInformation: String? = ""
) : Parcelable