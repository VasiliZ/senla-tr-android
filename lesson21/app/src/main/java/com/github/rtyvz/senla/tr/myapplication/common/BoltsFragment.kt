package com.github.rtyvz.senla.tr.myapplication.common

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.models.*
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class BoltsFragment : Fragment() {
    private lateinit var userEmail: String
    private lateinit var userPassword: String
    private val client = OkHttpClient()
    private val gsonBuilder = GsonBuilder().create()
    private var callBacks: ActivityCallBacks? = null

    companion object {
        val TAG: String = BoltsFragment::class.java.simpleName
        const val EXTRA_USER_EMAIL = "USER_EMAIL"
        const val EXTRA_USER_PASSWORD = "USER_PASSWORD"
        private const val EMPTY_STRING = ""
        private const val URL_WITHOUT_METHOD =
            "https://pub.zame-dev.org/senla-training-addition/lesson-21.php?method="
        private const val REQUEST_METHOD_LOGIN = "login"
        private const val REQUEST_METHOD_PROFILE = "profile"
        private const val STATUS_OK = "ok"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = activity as ActivityCallBacks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        userEmail = arguments?.getString(
            EXTRA_USER_EMAIL,
            EMPTY_STRING
        ) ?: EMPTY_STRING
        userPassword = arguments?.getString(
            EXTRA_USER_PASSWORD,
            EMPTY_STRING
        ) ?: EMPTY_STRING
        getTokenTask()
    }

    private fun getTokenTask(): Task<String> {
        return Task.callInBackground {
            return@callInBackground client.newCall(
                createRequest(
                    REQUEST_METHOD_LOGIN,
                    gsonBuilder.toJson(UserCredentials(userEmail, userPassword))
                )
            ).execute().body?.string()
        }.onSuccess {
            return@onSuccess gsonBuilder.fromJson(it.result, TokenResponse::class.java)
        }.onSuccessTask(Continuation<TokenResponse, Task<Result<UserProfileEntity>>> {
            if (it.result.status.contains(STATUS_OK)) {
                saveTokenTask(it.result)
                return@Continuation executeGetUserProfileTask(it.result.token)
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

    private fun saveTokenTask(result: TokenResponse?): Task<Unit>? {
        return Task.call {
            result?.let {
                callBacks?.saveToken(it.token)
            }
        }
    }

    private fun createRequest(method: String, json: String) = Request.Builder().url(
        StringBuilder(URL_WITHOUT_METHOD).append(
            method
        ).toString()
    ).post(json.toRequestBody()).build()


    private fun executeGetUserProfileTask(token: String): Task<Result<UserProfileEntity>>? {
        return Task.callInBackground {
            client.newCall(
                createRequest(
                    REQUEST_METHOD_PROFILE,
                    gsonBuilder.toJson(TokenEntity(token))
                )
            ).execute().body?.string()
        }.onSuccess {
            return@onSuccess gsonBuilder.fromJson(
                it.result,
                UserProfileResponse::class.java
            )
        }.continueWith {
            if (it.result.responseStatus.contains(STATUS_OK)) {
                return@continueWith Result.Success(it.result.toUserProfileEntity(userEmail))
            } else {
                return@continueWith Result.Error(it.result.message?: EMPTY_STRING)
            }
        }
    }

    override fun onDetach() {
        callBacks = null

        super.onDetach()
    }
}

interface ActivityCallBacks {
    fun saveToken(token: String)
    fun error(errorMsg: String)
    fun saveUserProfile(result: Result<UserProfileEntity>)
}