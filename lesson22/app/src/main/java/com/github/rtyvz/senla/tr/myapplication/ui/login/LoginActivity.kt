package com.github.rtyvz.senla.tr.myapplication.ui.login

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.databinding.LoginActivityBinding
import com.github.rtyvz.senla.tr.myapplication.models.State
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.providers.TaskProvider
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private val regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z-.]+\$".toRegex()
    private var progressDialog: ProgressDialog? = null
    private var localBroadcastManager: LocalBroadcastManager? = null
    private lateinit var userProfileReceiver: BroadcastReceiver
    private lateinit var userTokenReceiver: BroadcastReceiver
    private lateinit var userTokenErrorReceiver: BroadcastReceiver
    private lateinit var taskIsFaultReceiver: BroadcastReceiver

    companion object {
        const val BROADCAST_FETCH_USER_PROFILE = "local:BROADCAST_FETCH_USER_PROFILE"
        const val BROADCAST_TOKEN = "local:BROADCAST_TOKEN"
        const val BROADCAST_TOKEN_RESPONSE_ERROR = "local:BROADCAST_TOKEN_RESPONSE_ERROR"
        const val BROADCAST_TASK_IS_FAULTED = "local:BROADCAST_TASK_IS_FAULT"
        const val EXTRA_FETCH_USER_PROFILE = "FETCH_USER_PROFILE"
        const val EXTRA_USER_TOKEN = "USER_TOKEN"
        const val EXTRA_TASK_IS_FAULTED = "TASK_IS_FAULT"
        const val EXTRA_USER_TOKEN_ERROR = "USER_TOKEN_ERROR"
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        initProfileReceiver()
        initTokenReceiver()
        initTokenErrorResponseReceiver()
        initFaultTaskReceiver()
        initProgress()

        val state = App.INSTANCE.state
        if (state == null) {
            App.INSTANCE.state = State()
        }

        binding.apply {
            loginButton.setOnClickListener {
                when {
                    userEmailEditText.text.isNullOrBlank() || userPasswordEditText.text.isNullOrBlank() ->
                        errorTextView.text =
                            getString(R.string.login_activity_fields_is_empty_error)
                    !userEmailEditText.text.toString().matches(regexEmail) -> {
                        errorTextView.text =
                            getString(R.string.login_activity_it_isnt_email_error)
                    }
                    else -> {
                        progressDialog?.show()
                        errorTextView.text =
                            EMPTY_STRING
                        App.INSTANCE.state?.isTaskRunning = true
                        TaskProvider.getTokenTask().initTokenTask(
                            binding.userEmailEditText.text.toString(),
                            binding.userPasswordEditText.text.toString()
                        )
                    }
                }
            }
        }
    }

    private fun initFaultTaskReceiver() {
        taskIsFaultReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                progressDialog?.dismiss()
                binding.errorTextView.text = getString(R.string.login_activity_request_error)
            }
        }
    }

    private fun initTokenErrorResponseReceiver() {
        userTokenErrorReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                progressDialog?.dismiss()
                App.INSTANCE.state?.isTaskRunning = false
                binding.errorTextView.text = intent?.getStringExtra(EXTRA_USER_TOKEN_ERROR)
            }
        }
    }

    private fun initTokenReceiver() {
        userTokenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                App.INSTANCE.state?.token = intent?.getStringExtra(EXTRA_USER_TOKEN) ?: EMPTY_STRING
            }
        }
    }

    private fun initProfileReceiver() {
        userProfileReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                App.INSTANCE.state?.isTaskRunning = false
                App.INSTANCE.state?.email = binding.userEmailEditText.text.toString()
                saveUserProfile(intent?.getParcelableExtra(EXTRA_FETCH_USER_PROFILE))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        registerProfileReceiver()
        registerTokenReceiver()
        registerErrorTokenResponseReceiver()
        registerRequestErrorReceiver()

        if (App.INSTANCE.state?.isTaskRunning == true) {
            progressDialog?.show()
        }
    }

    private fun registerRequestErrorReceiver() {
        localBroadcastManager?.registerReceiver(
            taskIsFaultReceiver, IntentFilter(
                BROADCAST_TASK_IS_FAULTED
            )
        )
    }

    private fun registerErrorTokenResponseReceiver() {
        localBroadcastManager?.registerReceiver(
            userTokenErrorReceiver, IntentFilter(BROADCAST_TOKEN_RESPONSE_ERROR)
        )
    }

    private fun registerProfileReceiver() {
        localBroadcastManager?.registerReceiver(
            userProfileReceiver, IntentFilter(BROADCAST_FETCH_USER_PROFILE)
        )
    }

    private fun registerTokenReceiver() {
        localBroadcastManager?.registerReceiver(userTokenReceiver, IntentFilter(BROADCAST_TOKEN))
    }

    private fun initProgress() {
        progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.login_activity_wait_label))
            setCanceledOnTouchOutside(false)
            create()
        }
    }

    private fun saveUserProfile(result: Result<UserProfileEntity>?) {
        when (result) {
            is Result.Success -> {
                progressDialog?.dismiss()
                App.INSTANCE.state?.userProfile = result.responseBody
                App.INSTANCE.state?.isTaskRunning = false
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            is Result.Error -> binding.errorTextView.text = result.error
        }
    }

    override fun onPause() {
        localBroadcastManager?.unregisterReceiver(userTokenReceiver)
        localBroadcastManager?.unregisterReceiver(userProfileReceiver)
        localBroadcastManager?.unregisterReceiver(userTokenErrorReceiver)
        localBroadcastManager?.unregisterReceiver(taskIsFaultReceiver)
        progressDialog?.dismiss()

        super.onPause()
    }
}