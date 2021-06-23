package com.github.rtyvz.senla.tr.myapplication.network

import com.github.rtyvz.senla.tr.myapplication.models.TokenResponse
import okhttp3.Call
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("lesson-21.php?method=login")
    fun getUserToken(
        @Body requestTokenBody: RequestBody
    ):Response<TokenResponse>

    @POST("lesson-21.php?method=profile")
    fun getUserProfile(
        @Body requestUserProfileBody:RequestBody
    ):Call
}