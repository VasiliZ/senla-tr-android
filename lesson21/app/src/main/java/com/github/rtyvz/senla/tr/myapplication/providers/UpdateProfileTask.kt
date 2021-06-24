package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.models.TokenRequest
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileResponse
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class UpdateProfileTask(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val context: Context
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
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)
        return Task.callInBackground {
            client.newCall(
                createRequest(
                    gson.toJson(TokenRequest(token))
                )
            ).execute().body?.string()
        }.onSuccess {
            return@onSuccess gson.fromJson(
                it.result,
                UserProfileResponse::class.java
            )
        }.continueWith {
            if (it.result.responseStatus.contains(STATUS_OK)) {
                localBroadcastManager
                    .sendBroadcast(Intent(ProfileActivity.BROADCAST_USER_PROFILE).apply {
                        putExtras(Bundle().apply {
                            putParcelable(
                                ProfileActivity.EXTRA_USER_PROFILE,
                                it.result.toUserProfileEntity(userEmail)
                            )
                        })
                    })
                return@continueWith Result.Success(it.result.toUserProfileEntity(userEmail))
            } else {
                return@continueWith Result.Error(it.result.message ?: EMPTY_STRING)
            }
        }
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