package com.github.rtyvz.senla.tr.myapplication.ui.login

import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.common.ActivityCallBacks
import com.github.rtyvz.senla.tr.myapplication.common.BoltsFragment
import com.github.rtyvz.senla.tr.myapplication.databinding.LoginActivityBinding
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.github.rtyvz.senla.tr.myapplication.utils.getString
import com.github.rtyvz.senla.tr.myapplication.utils.putString

class LoginActivity : AppCompatActivity(),
    ActivityCallBacks {
    private lateinit var binding: LoginActivityBinding
    private val regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z-.]+\$".toRegex()
    private var progressDialog: ProgressDialog? = null
    private lateinit var prefs: SharedPreferences
    private var localBroadcastManager: LocalBroadcastManager? = null
    private lateinit var runningTaskReceiver: BroadcastReceiver

    companion object {
        const val PREFS_USER = "PREFS_USER"
        const val SAVED_EMAIL = "EMAIL"
        const val SAVED_TOKEN = "TOKEN"
        const val BROADCAST_RUNNING_TASK_FLAG = "local:BROADCAST_RUNNING_TASK_FLAG"
        const val EXTRA_RUNNING_TASK_FLAG = "RUNNING_TASK_FLAG"
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        prefs = getSharedPreferences(PREFS_USER, Context.MODE_PRIVATE)

        if (prefs.getString(SAVED_TOKEN) == EMPTY_STRING) {
            setContentView(binding.root)

            localBroadcastManager = LocalBroadcastManager.getInstance(this)
            initProgress()

            if (App.INSTANCE.getState().isTaskRunning) {
                progressDialog?.show()
            }

            initRunningTaskReceiver()
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

                            var fragment =
                                supportFragmentManager.findFragmentByTag(BoltsFragment.TAG)

                            if (fragment == null) {
                                fragment = BoltsFragment
                                    .newInstance(
                                        binding.userEmailEditText.text.toString(),
                                        binding.userPasswordEditText.text.toString()
                                    )
                                supportFragmentManager
                                    .beginTransaction()
                                    .add(
                                        fragment,
                                        BoltsFragment.TAG
                                    ).commit()
                            } else {
                                (fragment as BoltsFragment).getTokenTask()
                            }
                        }
                    }
                }
            }
        } else {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
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
                App.INSTANCE.getState().isTaskRunning = intent?.getBooleanExtra(
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

    override fun saveToken(token: String) {
        prefs.putString(SAVED_TOKEN, token)
    }

    override fun error(errorMsg: String) {
        progressDialog?.dismiss()
        binding.errorTextView.text = errorMsg
    }

    override fun saveUserProfile(result: Result<UserProfileEntity>) {
        when (result) {
            is Result.Success -> {
                saveUserEmail()
                progressDialog?.dismiss()
                App.INSTANCE.getState().isTaskRunning = false
                startProfileActivity(result.responseBody)
                finish()
            }

            is Result.Error -> {
                binding.errorTextView.text = result.error
                progressDialog?.dismiss()
            }
        }
    }

    private fun saveUserEmail() {
        prefs.putString(SAVED_EMAIL, binding.userEmailEditText.text.toString())
    }

    private fun startProfileActivity(profileResponseData: UserProfileEntity) {
        startActivity(Intent(this, ProfileActivity::class.java).apply {
            putExtras(Bundle().apply {
                putParcelable(ProfileActivity.EXTRA_USER_PROFILE, profileResponseData)
            })
        })
    }

    override fun onStop() {
        localBroadcastManager?.unregisterReceiver(runningTaskReceiver)
        progressDialog?.dismiss()

        super.onStop()
    }
}