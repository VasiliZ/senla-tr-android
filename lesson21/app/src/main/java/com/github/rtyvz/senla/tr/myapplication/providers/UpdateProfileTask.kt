package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.models.Result
import com.github.rtyvz.senla.tr.myapplication.models.TokenRequest
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileResponse
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateProfileTask(
    private val client: OkHttpClient,
    private val gson: Gson
) {
    companion object {
        private const val METHOD_NAME = "method"
        private const val STATUS_OK = "ok"
        private const val URI_SCHEME = "https"
        private const val EMPTY_STRING = ""
        private const val URI_AUTHORITY = "pub.zame-dev.org"
        private const val URI_FIRST_PART_PATH = "senla-training-addition"
        private const val URI_SECOND_PART_PATH = "lesson-20.php"
        private const val REQUEST_METHOD_PROFILE = "profile"
    }

    fun executeUpdateUserProfileTask(
        token: String,
        userEmail: String
    ): Task<Result<UserProfileEntity>>? {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)

        return Task.callInBackground {
            client.newCall(createRequest(gson.toJson(TokenRequest(token))))
                .execute().body?.string()
        }.continueWith(Continuation {
            val profileResponse = gson.fromJson(it.result, UserProfileResponse::class.java)

            if (profileResponse.responseStatus.contains(STATUS_OK)) {
                localBroadcastManager
                    .sendBroadcastSync(Intent(ProfileActivity.BROADCAST_USER_PROFILE).apply {
                        putExtras(Bundle().apply {
                            putParcelable(
                                ProfileActivity.EXTRA_USER_PROFILE,
                                profileResponse.toUserProfileEntity(userEmail)
                            )
                        })
                    })
                return@Continuation Result.Success(profileResponse.toUserProfileEntity(userEmail))
            } else {
                return@Continuation Result.Error(profileResponse.message ?: EMPTY_STRING)
            }
        }, Task.UI_THREAD_EXECUTOR)
    }

    private fun createRequest(json: String) = Request.Builder().url(
        Uri.Builder().scheme(URI_SCHEME)
            .authority(URI_AUTHORITY)
            .appendPath(URI_FIRST_PART_PATH)
            .appendPath(URI_SECOND_PART_PATH)
            .appendQueryParameter(
                METHOD_NAME,
                REQUEST_METHOD_PROFILE
            ).build().toString()
    ).post(json.toRequestBody()).build()
}