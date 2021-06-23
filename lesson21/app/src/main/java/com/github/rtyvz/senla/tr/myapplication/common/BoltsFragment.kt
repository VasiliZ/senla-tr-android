package com.github.rtyvz.senla.tr.myapplication.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.models.*
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class BoltsFragment : Fragment() {
    private var userEmail: String? = null
    private lateinit var userPassword: String
    private val client = OkHttpClient()
    private val gsonBuilder = GsonBuilder().create()
    private var callBacks: ActivityCallBacks? = null
    private lateinit var localBroadcastManager: LocalBroadcastManager

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

        fun newInstance(email: String, password: String) = BoltsFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_USER_EMAIL, email)
                putString(EXTRA_USER_PASSWORD, password)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is ActivityCallBacks) {
            callBacks = activity as ActivityCallBacks
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true
        localBroadcastManager = LocalBroadcastManager.getInstance(requireContext())
        userEmail = arguments?.getString(
            EXTRA_USER_EMAIL,
            EMPTY_STRING
        )
        userPassword = arguments?.getString(
            EXTRA_USER_PASSWORD,
            EMPTY_STRING
        ) ?: EMPTY_STRING

        if (arguments != null) {
            getTokenTask()
        }
    }

    fun getTokenTask(): Task<String> {
        localBroadcastManager.sendBroadcast(
            Intent(LoginActivity.BROADCAST_RUNNING_TASK_FLAG).apply {
                putExtra(LoginActivity.EXTRA_RUNNING_TASK_FLAG, true)
            })

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
                it.result?.let {
                    callBacks?.saveToken(it.token)
                }
                return@Continuation executeFetchUserProfileTask(it.result.token)
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
        StringBuilder(URL_WITHOUT_METHOD).append(
            method
        ).toString()
    ).post(json.toRequestBody()).build()


    fun executeFetchUserProfileTask(token: String): Task<Result<UserProfileEntity>>? {
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
                localBroadcastManager
                    .sendBroadcast(Intent(ProfileActivity.BROADCAST_USER_PROFILE).apply {
                        putExtras(Bundle().apply {
                            putParcelable(
                                ProfileActivity.EXTRA_USER_PROFILE,
                                it.result.toUserProfileEntity(userEmail)
                            )
                        })
                    })
                return@continueWith Result.Success(it.result.toUserProfileEntity(userEmail))
            } else {
                return@continueWith Result.Error(it.result.message ?: EMPTY_STRING)
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