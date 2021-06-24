package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.common.BoltsFragment
import com.github.rtyvz.senla.tr.myapplication.models.TokenResponse
import com.github.rtyvz.senla.tr.myapplication.models.UserCredentials
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class TokenProvider(
    private val okHttpClient: OkHttpClient,
    private val context: Context,
    private val gsonBuilder: GsonBuilder,
    private val url:String
) {
    companion object{

    }
    private fun initTokenTask(userEmail: String, userPassword: String): Task<String> {
        LocalBroadcastManager.getInstance(context).sendBroadcast(
            Intent(LoginActivity.BROADCAST_RUNNING_TASK_FLAG).apply {
                putExtra(LoginActivity.EXTRA_RUNNING_TASK_FLAG, true)
            })

        return Task.callInBackground {
            return@callInBackground okHttpClient.newCall(
                createRequest(
                    BoltsFragment.REQUEST_METHOD_LOGIN,
                    gsonBuilder.toJson(UserCredentials(userEmail, userPassword))
                )
            ).execute().body?.string()
        }.onSuccess {
            return@onSuccess gsonBuilder.fromJson(it.result, TokenResponse::class.java)
        }.onSuccessTask(Continuation<TokenResponse, Task<Result<UserProfileEntity>>> {
            if (it.result.status.contains(BoltsFragment.STATUS_OK)) {
                it.result?.let {
                    callBacks?.saveToken(it.token)
                }
                return@Continuation executeUpdateUserProfileTask(it.result.token)
            } else {
                callBacks?.error(it.result.message)
                return@Continuation null
            }
        }).continueWith {
            if (it.isFaulted) {
                callBacks?.error(getString(R.string.bolts_fragment_request_error))
                return@continueWith null
            } else {
                callBacks?.saveUserProfile(it.result)
                return@continueWith null
            }
        }
    }

    private fun createRequest(method: String, json: String) = Request.Builder().url(
        StringBuilder(BoltsFragment.URL_WITHOUT_METHOD).append(
            method
        ).toString()
    ).post(json.toRequestBody()).build()

}