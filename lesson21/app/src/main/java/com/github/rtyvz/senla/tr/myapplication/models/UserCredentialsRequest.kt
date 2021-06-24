package com.github.rtyvz.senla.tr.myapplication.models

import com.google.gson.annotations.SerializedName

data class UserCredentialsRequest(
    @SerializedName("email")
    val userEmail: String,
    @SerializedName("password")
    val userPassword: String
)