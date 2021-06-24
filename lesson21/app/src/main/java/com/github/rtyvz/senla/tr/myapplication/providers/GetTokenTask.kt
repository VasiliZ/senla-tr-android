package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.models.TokenResponse
import com.github.rtyvz.senla.tr.myapplication.models.UserCredentialsRequest
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class GetTokenTask(
    private val okHttpClient: OkHttpClient,
    private val context: Context,
    private val gson: Gson
) {

    companion object {
        private const val METHOD_NAME = "method"
        private const val REQUEST_METHOD_LOGIN = "login"
        private const val STATUS_OK = "ok"
        private const val URI_SCHEME = "https"
        private const val URI_AUTHORITY = "pub.zame-dev.org"
        private const val URI_FIRST_PART_PATH = "senla-training-addition"
        private const val URI_SECOND_PART_PATH = "lesson-20.php"
    }

    fun initTokenTask(userEmail: String, userPassword: String): Task<String> {
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)

        return Task.callInBackground {
            return@callInBackground okHttpClient.newCall(
                createRequest(
                    gson.toJson(UserCredentialsRequest(userEmail, userPassword))
                )
            ).execute().body?.string()
        }.onSuccess {
            return@onSuccess gson.fromJson(it.result, TokenResponse::class.java)
        }.onSuccessTask(Continuation<TokenResponse, Task<Result<UserProfileEntity>>> {
            if (it.result.status.contains(STATUS_OK)) {
                it.result?.let {
                    localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_TOKEN).apply {
                        putExtra(LoginActivity.EXTRA_USER_TOKEN, it.token)
                    })
                }
                return@Continuation TaskProvider.getProfileTask()
                    .executeUpdateUserProfileTask(it.result.token, userEmail)
            } else {
                localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_TOKEN_RESPONSE_ERROR).apply {
                    putExtra(LoginActivity.EXTRA_USER_TOKEN_ERROR, it.result.message)
                })
                return@Continuation null
            }
        }).continueWith {
            if (it.isFaulted) {
                localBroadcastManager.sendBroadcast(Intent(
                    LoginActivity.BROADCAST_TASK_IS_FAULTED
                ).apply {
                    putExtra(LoginActivity.EXTRA_TASK_IS_FAULTED, it.result)
                })
                return@continueWith null
            } else {
                localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_FETCH_USER_PROFILE).apply {
                    putExtra(LoginActivity.EXTRA_FETCH_USER_PROFILE, it.result)
                })
                return@continueWith null
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
                REQUEST_METHOD_LOGIN
            ).build().toString()
    ).post(json.toRequestBody()).build()
}