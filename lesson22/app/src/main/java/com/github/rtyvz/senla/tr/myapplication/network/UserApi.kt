package com.github.rtyvz.senla.tr.myapplication.network

import com.github.rtyvz.senla.tr.myapplication.models.TokenRequest
import com.github.rtyvz.senla.tr.myapplication.models.TokenResponse
import com.github.rtyvz.senla.tr.myapplication.models.UserCredentials
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("lesson-21.php?method=login")
    fun getUserToken(
        @Body credentials: UserCredentials
    ): Call<TokenResponse>

    @POST("lesson-21.php?method=profile")
    fun getUserProfile(
        @Body tokenRequest: TokenRequest
    ): Call<UserProfileResponse>
}