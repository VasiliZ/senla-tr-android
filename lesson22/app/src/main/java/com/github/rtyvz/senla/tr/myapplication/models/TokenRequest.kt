package com.github.rtyvz.senla.tr.myapplication.models

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token")
    val token: String
)
