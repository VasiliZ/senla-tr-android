package com.github.rtyvz.senla.tr.myapplication.providers

import android.content.Intent
import android.os.Bundle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.models.TokenRequest
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.network.UserApi
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result

class UpdateProfileTask(
    private val api: UserApi
) {
    companion object {
        private const val STATUS_OK = "ok"
        private const val EMPTY_STRING = ""
    }

    fun executeUpdateUserProfileTask(
        token: String?,
        userEmail: String
    ): Task<Result<UserProfileEntity>>? {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)

        return Task.callInBackground {
            api.getUserProfile(tokenRequest = TokenRequest(token ?: EMPTY_STRING))
                .execute()
                .body()
        }.onSuccess {
            return@onSuccess it.result
        }.continueWith {
            if (it.result?.responseStatus?.contains(STATUS_OK) == true) {
                localBroadcastManager
                    .sendBroadcast(Intent(ProfileActivity.BROADCAST_USER_PROFILE).apply {
                        putExtras(Bundle().apply {
                            putParcelable(
                                ProfileActivity.EXTRA_USER_PROFILE,
                                it.result?.toUserProfileEntity(userEmail)
                            )
                        })
                    })
                return@continueWith Result.Success(it.result?.toUserProfileEntity(userEmail))
            } else {
                return@continueWith Result.Error(it.result?.message ?: EMPTY_STRING)
            }
        }
    }
}