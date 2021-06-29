package com.github.rtyvz.senla.tr.myapplication.models

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("status")
    val status: ResponseStatus,
    @SerializedName("token")
    val token: String,
    @SerializedName("message")
    val message: String
)