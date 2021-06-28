package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.models.*
import com.github.rtyvz.senla.tr.myapplication.network.UserApi
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity

class GetTokenTask(private val api: UserApi) {

    companion object {
        private const val STATUS_OK = "ok"
    }

    fun initTokenTask(userEmail: String, userPassword: String): Task<String> {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
        App.INSTANCE.state?.isTaskRunning = true

        return Task.callInBackground {
            return@callInBackground api.getUserToken(UserCredentials(userEmail, userPassword))
                .execute()
                .body()
        }.onSuccessTask(Continuation<TokenResponse?, Task<Result<UserProfileEntity>>> {
            val tokenResponse = it.result
            if (tokenResponse != null) {
                when (tokenResponse.status) {
                    ResponseStatus.OK.status -> {
                        App.INSTANCE.state?.token = tokenResponse.token

                        return@Continuation TaskProvider.getProfileTask()
                            .executeUpdateUserProfileTask(it.result?.token, userEmail)
                    }
                    ResponseStatus.ERROR.status -> {
                        localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_TOKEN_RESPONSE_ERROR).apply {
                            putExtra(LoginActivity.EXTRA_USER_TOKEN_ERROR, tokenResponse.message)
                        })
                        return@Continuation null
                    }
                    else -> {
                        localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_UNKNOWN_STATUS_ERROR).apply {
                            putExtra(LoginActivity.EXTRA_UNKNOWN_STATUS_ERROR, tokenResponse.message)
                        })
                        return@Continuation null
                    }
                }
            } else {
                localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_EMPTY_TOKEN_RESPONSE).apply {
                    putExtra(LoginActivity.BROADCAST_EMPTY_TOKEN_RESPONSE, tokenResponse?.message)
                })
                return@Continuation null
            }
        }, Task.BACKGROUND_EXECUTOR).continueWith(Continuation {
            if (it.isFaulted) {
                localBroadcastManager.sendBroadcast(Intent(
                    LoginActivity.BROADCAST_TASK_IS_FAULTED
                ).apply {
                    putExtra(LoginActivity.EXTRA_TASK_IS_FAULTED, it.result)
                })
                return@Continuation null
            } else {
                localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_FETCH_USER_PROFILE).apply {
                    putExtra(LoginActivity.EXTRA_FETCH_USER_PROFILE, it.result)
                })
                return@Continuation null
            }
        }, Task.UI_THREAD_EXECUTOR)
    }
}