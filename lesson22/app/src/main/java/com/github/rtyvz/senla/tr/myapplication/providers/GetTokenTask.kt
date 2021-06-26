package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.models.TokenResponse
import com.github.rtyvz.senla.tr.myapplication.models.UserCredentials
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.network.UserApi
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result

class GetTokenTask(
    private val api: UserApi
) {

    companion object {
        private const val STATUS_OK = "ok"
    }

    fun initTokenTask(userEmail: String, userPassword: String): Task<String> {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)

        return Task.callInBackground {
            return@callInBackground api.getUserToken(UserCredentials(userEmail, userPassword))
                .execute()
                .body()
        }.onSuccess {
            return@onSuccess it.result
        }.onSuccessTask(Continuation<TokenResponse?, Task<Result<UserProfileEntity>>> {
            if (it.result?.status?.contains(STATUS_OK) == true) {
                it.result?.let {
                    localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_TOKEN).apply {
                        putExtra(LoginActivity.EXTRA_USER_TOKEN, it.token)
                    })
                }
                return@Continuation TaskProvider.getProfileTask()
                    .executeUpdateUserProfileTask(it.result?.token, userEmail)
            } else {
                localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_TOKEN_RESPONSE_ERROR).apply {
                    putExtra(LoginActivity.EXTRA_USER_TOKEN_ERROR, it.result?.message)
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
}