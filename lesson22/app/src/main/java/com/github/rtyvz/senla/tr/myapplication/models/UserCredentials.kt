package com.github.rtyvz.senla.tr.myapplication.models

import com.google.gson.annotations.SerializedName

data class UserCredentials(
    @SerializedName("email")
    val userEmail: String? = null,
    @SerializedName("password")
    val userPassword: String
)