package com.github.rtyvz.senla.tr.loginapp

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FragmentWithTasks : Fragment() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private var loginTask: LoginTask? = null
    private var getUserProfileTask: GetUserProfileTask? = null
    private val httpClient = OkHttpClient()

    companion object {
        const val TAG = "FragmentWithTasks"
        const val EXTRA_USER_EMAIL = "USER_EMAIL"
        const val EXTRA_USER_PASSWORD = "USER_PASSWORD"
        private const val EMPTY_STRING = ""
        private const val LINK_WITHOUT_METHOD =
            "https://pub.zame-dev.org/senla-training-addition/lesson-20.php?method="
        private const val LOGIN_METHOD = "login"
        private const val PROFILE_METHOD = "profile"
        private const val JSON_EMAIL_FIELD = "email"
        private const val JSON_PASSWORD_FIELD = "password"
        private const val JSON_TOKEN_FIELD = "token"
        private const val STATUS_FIELD_RESPONSE = "status"
        private const val ERROR_FIELD_RESPONSE = "error"
        private const val MESSAGE_FIELD_RESPONSE = "message"
        private const val TOKEN_FIELD_RESPONSE = "token"
        private const val FIRST_NAME_FIELD_RESPONSE = "firstName"
        private const val LAST_NAME_FIELD_RESPONSE = "lastName"
        private const val BIRTH_DATE_FIELD_RESPONSE = "birthDate"
        private const val NOTES_FIELD_RESPONSE = "notes"

    }

    private var callbacks: TaskCallbacks? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
        arguments?.let {
            userEmail = it.getString(EXTRA_USER_EMAIL, EMPTY_STRING)
            userPassword = it.getString(EXTRA_USER_PASSWORD, EMPTY_STRING)
        }

        startTask()
    }

    private fun startTask() {
        loginTask = LoginTask()
        loginTask?.execute()
    }

    fun restartLoginTask(email: String, password: String) {
        userEmail = email
        userPassword = password
        startTask()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = (activity as LoginActivity)
        when {
            loginTask?.status == AsyncTask.Status.RUNNING -> callbacks?.taskRunningYet()
            getUserProfileTask?.status == AsyncTask.Status.RUNNING -> callbacks?.taskRunningYet()
        }
    }

    override fun onDetach() {
        callbacks = null

        super.onDetach()
    }

    inner class LoginTask : AsyncTask<Void, Void, Result<UserTokenResponse>>() {
        override fun doInBackground(vararg params: Void?): Result<UserTokenResponse>? {
            try {
                httpClient.newCall(prepareRequest()).execute().use {
                    it.body?.let { body ->
                        val jsonObject = JSONObject(body.string())
                        if (jsonObject.getString(STATUS_FIELD_RESPONSE).contains(
                                ERROR_FIELD_RESPONSE
                            )
                        ) {
                            return Result.Error(jsonObject.getString(MESSAGE_FIELD_RESPONSE))
                        }
                        val token = jsonObject.getString(TOKEN_FIELD_RESPONSE)
                        getUserProfileTask = GetUserProfileTask(token)
                        getUserProfileTask?.execute()
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
                        activity?.getString(R.string.task_fragment_try_get_token_error) ?: it
                    )
                }
            }

            return null
        }

        private fun prepareRequest(): Request {
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
                callbacks?.saveToken(it)
            }
        }
    }

    inner class GetUserProfileTask(private val token: String) :
        AsyncTask<Void, Void, Result<UserProfileResponse>>() {
        override fun doInBackground(vararg params: Void?): Result<UserProfileResponse>? {
            val requestBody = userTokenToJson(token).toRequestBody()
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
                    val userProfile = UserProfileResponse(
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
                (activity as LoginActivity).requestResult(it)
            }
        }

        private fun userTokenToJson(token: String) =
            JSONObject().put(JSON_TOKEN_FIELD, token).toString()
    }
}

interface TaskCallbacks {
    fun saveToken(result: Result<UserTokenResponse>)
    fun requestResult(response: Result<UserProfileResponse>)
    fun taskRunningYet()
}