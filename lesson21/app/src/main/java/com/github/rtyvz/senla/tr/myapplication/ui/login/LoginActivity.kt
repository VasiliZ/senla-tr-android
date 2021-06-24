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
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private val regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z-.]+\$".toRegex()
    private var progressDialog: ProgressDialog? = null
    private var localBroadcastManager: LocalBroadcastManager? = null
    private lateinit var runningTaskReceiver: BroadcastReceiver
    private lateinit var userProfileReceiver: BroadcastReceiver
    private lateinit var userTokenReceiver: BroadcastReceiver

    companion object {
        const val BROADCAST_USER_PROFILE = "local:BROADCAST_USER_PROFILE"
        const val BROADCAST_RUNNING_TASK_FLAG = "local:BROADCAST_RUNNING_TASK_FLAG"
        const val BROADCAST_TOKEN = "local:TOKEN"
        const val EXTRA_USER_PROFILE = "USER_PROFILE"
        const val EXTRA_USER_TOKEN = "USER_TOKEN"
        const val EXTRA_RUNNING_TASK_FLAG = "RUNNING_TASK_FLAG"
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        initProfileReceiver()
        initTokenReceiver()
        initRunningTaskReceiver()
        initProgress()
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
                        App.TaskProvider.getTokenTask().initTokenTask(
                            binding.userEmailEditText.text.toString(),
                            binding.userPasswordEditText.text.toString()
                        )
                    }
                }
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
                saveUserProfile(intent?.getParcelableExtra(EXTRA_USER_PROFILE))
            }
        }
    }

    override fun onResume() {
        super.onResume()

        registerProfileReceiver()

    }

    private fun registerProfileReceiver() {
        localBroadcastManager?.registerReceiver(
            userProfileReceiver, IntentFilter(
                BROADCAST_USER_PROFILE
            )
        )
    }

    private fun initProgress() {
        progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.login_activity_wait_label))
            setCanceledOnTouchOutside(false)
            create()
        }
    }

    private fun initRunningTaskReceiver() {
        runningTaskReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                App.INSTANCE.state?.isTaskRunning = intent?.getBooleanExtra(
                    EXTRA_RUNNING_TASK_FLAG, false
                ) ?: false
            }
        }
        localBroadcastManager?.registerReceiver(
            runningTaskReceiver, IntentFilter(
                BROADCAST_RUNNING_TASK_FLAG
            )
        )
    }

    private fun saveUserProfile(result: Result<UserProfileEntity>?) {
        when (result) {
            is Result.Success -> {
                progressDialog?.dismiss()
                App.INSTANCE.state?.isTaskRunning = false
                startProfileActivity(result.responseBody)
                finish()
            }
            is Result.Error -> binding.errorTextView.text = result.error
        }
    }

    private fun startProfileActivity(profileResponseData: UserProfileEntity) {
        startActivity(Intent(this, ProfileActivity::class.java).apply {
            putExtras(Bundle().apply {
                putParcelable(ProfileActivity.EXTRA_USER_PROFILE, profileResponseData)
            })
        })
    }

    override fun onPause() {
        localBroadcastManager?.unregisterReceiver(runningTaskReceiver)
        localBroadcastManager?.unregisterReceiver(userProfileReceiver)
        progressDialog?.dismiss()

        super.onPause()
    }
}