package com.github.rtyvz.senla.tr.lesson_07.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInformation(
    val login: String,
    val password: String,
    val name: String? = null,
    val secondName: String? = null,
    val sex: String? = null,
    val additionalInformation: String? = null
) : Parcelable