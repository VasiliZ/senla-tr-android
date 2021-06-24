package com.github.rtyvz.senla.tr.loginapp.task

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import com.github.rtyvz.senla.tr.loginapp.utils.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class GetUserProfileTask(
    private val httpClient: OkHttpClient,
    private val localBroadcastManager: LocalBroadcastManager
) : AsyncTask<String, Void, Result<UserProfileResponse>>() {
    private lateinit var userEmail: String

    companion object {
        private const val LINK_WITHOUT_METHOD =
            "https://pub.zame-dev.org/senla-training-addition/lesson-20.php?method="

        private const val PROFILE_METHOD = "profile"

        private const val JSON_TOKEN_FIELD = "token"
        private const val STATUS_FIELD_RESPONSE = "status"
        private const val ERROR_FIELD_RESPONSE = "error"
        private const val MESSAGE_FIELD_RESPONSE = "message"

        private const val FIRST_NAME_FIELD_RESPONSE = "firstName"
        private const val LAST_NAME_FIELD_RESPONSE = "lastName"
        private const val BIRTH_DATE_FIELD_RESPONSE = "birthDate"
        private const val NOTES_FIELD_RESPONSE = "notes"
    }

    override fun doInBackground(vararg params: String?): Result<UserProfileResponse>? {
        val requestBody = userTokenToJson(params[0].toString()).toRequestBody()
        userEmail = params[1].toString()
        val request = Request.Builder().url(
            StringBuilder(LINK_WITHOUT_METHOD).append(
                PROFILE_METHOD
            ).toString()
        ).post(requestBody)
            .build()

        httpClient.newCall(request).execute().use {
            it.body?.let { body ->
                val jsonObject = JSONObject(body.string())
                if (jsonObject.getString(STATUS_FIELD_RESPONSE)
                        .contains(ERROR_FIELD_RESPONSE)
                ) {
                    return Result.Error(jsonObject.getString(MESSAGE_FIELD_RESPONSE))
                }
                val userProfile =
                    UserProfileResponse(
                        email = userEmail,
                        firstName = jsonObject.getString(FIRST_NAME_FIELD_RESPONSE),
                        lastName = jsonObject.getString(LAST_NAME_FIELD_RESPONSE),
                        birthDate = jsonObject.getLong(BIRTH_DATE_FIELD_RESPONSE),
                        notes = jsonObject.getString(NOTES_FIELD_RESPONSE)
                    )
                return Result.Success(userProfile)
            }
        }
        return null
    }

    override fun onPostExecute(result: Result<UserProfileResponse>?) {
        super.onPostExecute(result)
        result?.let {
            localBroadcastManager.sendBroadcast(Intent(LoginActivity.BROADCAST_USER_PROFILE).apply {
                putExtra(LoginActivity.EXTRA_USER_PROFILE, it)
            })
        }
    }

    private fun userTokenToJson(token: String) =
        JSONObject().put(JSON_TOKEN_FIELD, token).toString()
}
