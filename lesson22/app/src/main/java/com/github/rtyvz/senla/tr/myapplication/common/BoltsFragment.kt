package com.github.rtyvz.senla.tr.myapplication.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.models.*
import com.github.rtyvz.senla.tr.myapplication.ui.login.LoginActivity
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.google.gson.GsonBuilder
import okhttp3.RequestBody.Companion.toRequestBody

class BoltsFragment : Fragment() {
    private var userEmail: String? = null
    private lateinit var userPassword: String
    private val gsonBuilder = GsonBuilder().create()
    private var callBacks: ActivityCallBacks? = null
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private val userApi = App.INSTANCE.getApi()

    companion object {
        val TAG: String = BoltsFragment::class.java.simpleName
        const val EXTRA_USER_EMAIL = "USER_EMAIL"
        const val EXTRA_USER_PASSWORD = "USER_PASSWORD"
        private const val EMPTY_STRING = ""
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
            return@callInBackground userApi.getUserToken(
                gsonBuilder.toJson(
                    UserCredentials(
                        userEmail,
                        userPassword
                    )
                ).toRequestBody()
            ).body()
        }.onSuccess {
                return@onSuccess it.result
        }.onSuccessTask(Continuation<TokenResponse?, Task<Result<UserProfileEntity>>> {
            it.result?.let {
                if (it.result?.status?.contains(STATUS_OK)) {
                    it.result?.let {
                        callBacks?.saveToken(it.token)
                    }
                    return@Continuation executeUpdateUserProfileTask(it.result?.token)
                } else {
                    callBacks?.error(it.result.message)
                    return@Continuation null
                }
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

    fun executeUpdateUserProfileTask(token: String): Task<Result<UserProfileEntity>>? {
        return Task.callInBackground {
            userApi.getUserProfile(gsonBuilder.toJson(TokenEntity(token)).toRequestBody())
                .execute().body?.string()
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