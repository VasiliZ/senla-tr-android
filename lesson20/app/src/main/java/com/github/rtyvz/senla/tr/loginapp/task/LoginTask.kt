package com.github.rtyvz.senla.tr.loginapp.task

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.login.entity.UserTokenResponse
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.utils.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginTask(
    private val httpClient: OkHttpClient,
    private val context: Context,
    private val localBroadcastManager: LocalBroadcastManager
) : AsyncTask<String, Void, Result<UserTokenResponse>>() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String

    companion object {
        private const val JSON_EMAIL_FIELD = "email"
        private const val JSON_PASSWORD_FIELD = "password"
        private const val TOKEN_FIELD_RESPONSE = "token"
        private const val LOGIN_METHOD = "login"
        private const val STATUS_FIELD_RESPONSE = "status"
        private const val ERROR_FIELD_RESPONSE = "error"
        private const val MESSAGE_FIELD_RESPONSE = "message"
        private const val EMPTY_STRING = ""
        private const val LINK_WITHOUT_METHOD =
            "https://pub.zame-dev.org/senla-training-addition/lesson-20.php?method="
    }

    override fun doInBackground(vararg params: String?): Result<UserTokenResponse>? {
        try {
            if (isCancelled) Thread.interrupted()
            userEmail = params[0].toString()
            userPassword = params[1].toString()
            httpClient.newCall(prepareTokenRequest()).execute().use {
                it.body?.let { body ->
                    val jsonObject = JSONObject(body.string())
                    if (jsonObject.getString(STATUS_FIELD_RESPONSE).contains(
                            ERROR_FIELD_RESPONSE
                        )
                    ) {
                        return Result.Error(jsonObject.getString(MESSAGE_FIELD_RESPONSE))
                    }
                    val token = jsonObject.getString(TOKEN_FIELD_RESPONSE)
                    return Result.Success(
                        UserTokenResponse(
                            token = token,
                            message = EMPTY_STRING
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.localizedMessage?.let {
                return Result.Error(
                    context.getString(R.string.task_fragment_try_get_token_error)
                )
            }
        }

        return null
    }

    private fun prepareTokenRequest(): Request {
        val requestBody = userCredentialsToJson().toRequestBody()
        return Request.Builder().url(
            StringBuilder(LINK_WITHOUT_METHOD).append(
                LOGIN_METHOD
            ).toString()
        ).post(requestBody)
            .build()
    }

    private fun userCredentialsToJson() = JSONObject()
        .put(JSON_EMAIL_FIELD, userEmail)
        .put(JSON_PASSWORD_FIELD, userPassword)
        .toString()

    override fun onPostExecute(result: Result<UserTokenResponse>?) {
        super.onPostExecute(result)
        result?.let {
            localBroadcastManager.sendBroadcastSync(Intent(LoginActivity.BROADCAST_USER_TOKEN).apply {
                putExtra(LoginActivity.EXTRA_USER_TOKEN, it)
            })
        }
    }
}