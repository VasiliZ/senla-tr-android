package com.github.rtyvz.senla.tr.myapplication.models

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("status")
    val responseStatus: String,
    @SerializedName("firstName")
    val firstUserName: String,
    @SerializedName("lastName")
    val lastUserName: String,
    @SerializedName("birthDate")
    val birthDate: String,
    @SerializedName("notes")
    val userNotes: String,
    @SerializedName("message")
    val message: String? = null
) {
    fun toUserProfileEntity(userEmail: String? = null): UserProfileEntity {
        return UserProfileEntity(
            userEmail, firstUserName, lastUserName, birthDate, userNotes, message
        )
    }
}