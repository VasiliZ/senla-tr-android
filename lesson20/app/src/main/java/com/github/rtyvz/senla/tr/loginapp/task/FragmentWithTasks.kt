package com.github.rtyvz.senla.tr.loginapp.task

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.login.entity.UserTokenResponse
import com.github.rtyvz.senla.tr.loginapp.login.ui.LoginActivity
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import com.github.rtyvz.senla.tr.loginapp.utils.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FragmentWithTasks : Fragment() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private var loginTask: LoginTask? = null
    private var userProfileTask: GetUserProfileTask? = null
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

        fun newInstance(email: String, password: String) = FragmentWithTasks().apply {
            arguments = bundleOf().apply {
                putString(
                    EXTRA_USER_EMAIL,
                    email
                )
                putString(
                    EXTRA_USER_PASSWORD,
                    password
                )
            }
        }

        class LoginTask(
            private val httpClient: OkHttpClient,
            private val context: FragmentActivity?,
            private val userEmail: String,
            private val userPassword: String,
            private val callbacks: TaskCallbacks?,
            private val userProfileTask: GetUserProfileTask?
        ) : AsyncTask<Void, Void, Result<UserTokenResponse>>() {

            override fun doInBackground(vararg params: Void?): Result<UserTokenResponse>? {
                try {
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
                            userProfileTask?.execute(token)
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
                            context?.getString(R.string.task_fragment_try_get_token_error) ?: it
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
                    callbacks?.saveToken(it)
                }
            }
        }

        class GetUserProfileTask(
            private val httpClient: OkHttpClient,
            private val userEmail: String,
            private val context: FragmentActivity?
        ) :
            AsyncTask<String, Void, Result<UserProfileResponse>>() {
            override fun doInBackground(vararg params: String?): Result<UserProfileResponse>? {
                val requestBody = userTokenToJson(params[0].toString()).toRequestBody()
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
                    (context as LoginActivity).requestResult(it)
                }
            }

            private fun userTokenToJson(token: String) =
                JSONObject().put(JSON_TOKEN_FIELD, token).toString()
        }
    }

    private var callbacks: TaskCallbacks? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
        arguments?.let {
            userEmail = it.getString(
                EXTRA_USER_EMAIL,
                EMPTY_STRING
            )
            userPassword = it.getString(
                EXTRA_USER_PASSWORD,
                EMPTY_STRING
            )
        }

        userProfileTask =
            GetUserProfileTask(httpClient, userEmail, activity)

        startTask()
    }

    private fun startTask() {
        loginTask =
            LoginTask(httpClient, activity, userEmail, userPassword, callbacks, userProfileTask)
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
            userProfileTask?.status == AsyncTask.Status.RUNNING -> callbacks?.taskRunningYet()
        }
    }

    override fun onDetach() {
        callbacks = null

        super.onDetach()
    }
}

interface TaskCallbacks {
    fun saveToken(result: Result<UserTokenResponse>)
    fun requestResult(response: Result<UserProfileResponse>)
    fun taskRunningYet()
}