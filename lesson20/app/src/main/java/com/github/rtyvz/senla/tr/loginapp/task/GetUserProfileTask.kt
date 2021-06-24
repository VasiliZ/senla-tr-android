package com.github.rtyvz.senla.tr.loginapp.task

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import com.github.rtyvz.senla.tr.loginapp.utils.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class GetUserProfileTask(
    private val httpClient: OkHttpClient
) : AsyncTask<String, Void, Result<UserProfileResponse>>() {
    private lateinit var userEmail: String

    companion object {
        private const val PROFILE_METHOD_PARAMETER = "profile"
        private const val JSON_TOKEN_FIELD = "token"
        private const val STATUS_FIELD_RESPONSE = "status"
        private const val ERROR_FIELD_RESPONSE = "error"
        private const val MESSAGE_FIELD_RESPONSE = "message"
        private const val FIRST_NAME_FIELD_RESPONSE = "firstName"
        private const val LAST_NAME_FIELD_RESPONSE = "lastName"
        private const val BIRTH_DATE_FIELD_RESPONSE = "birthDate"
        private const val NOTES_FIELD_RESPONSE = "notes"
        private const val PARAM_METHOD_NAME = "method"
        private const val URI_SCHEME = "https"
        private const val URI_AUTHORITY = "pub.zame-dev.org"
        private const val URI_FIRST_PART_PATH = "senla-training-addition"
        private const val URI_SECOND_PART_PATH = "lesson-20.php"
    }

    override fun doInBackground(vararg params: String?): Result<UserProfileResponse>? {
        val requestBody = convertUserTokenToJson(params[0].toString()).toRequestBody()
        userEmail = params[1].toString()

        val request = Request.Builder().url(
            Uri.Builder().scheme(URI_SCHEME)
                .authority(URI_AUTHORITY)
                .appendPath(URI_FIRST_PART_PATH)
                .appendPath(URI_SECOND_PART_PATH)
                .appendQueryParameter(PARAM_METHOD_NAME, PROFILE_METHOD_PARAMETER).build()
                .toString()
        ).post(requestBody)
            .build()

        httpClient.newCall(request).execute().use {
            it.body?.let { body ->
                val jsonObject = JSONObject(body.string())

                if (jsonObject.getString(STATUS_FIELD_RESPONSE)
                        .contains(ERROR_FIELD_RESPONSE)
                ) {
                    return Result.Error(jsonObject.optString(MESSAGE_FIELD_RESPONSE))
                }

                val userProfile =
                    UserProfileResponse(
                        email = userEmail,
                        firstName = jsonObject.optString(FIRST_NAME_FIELD_RESPONSE),
                        lastName = jsonObject.optString(LAST_NAME_FIELD_RESPONSE),
                        birthDate = jsonObject.optLong(BIRTH_DATE_FIELD_RESPONSE),
                        notes = jsonObject.optString(NOTES_FIELD_RESPONSE)
                    )

                return Result.Success(userProfile)
            }
        }
        return null
    }

    override fun onPostExecute(result: Result<UserProfileResponse>?) {
        super.onPostExecute(result)

        LocalBroadcastManager.getInstance(App.INSTANCE)
            .sendBroadcastSync(Intent(LoginActivity.BROADCAST_USER_PROFILE).apply {
                putExtra(LoginActivity.EXTRA_USER_PROFILE, result)
            })
    }

    private fun convertUserTokenToJson(token: String) =
        JSONObject().put(JSON_TOKEN_FIELD, token).toString()
}
