package com.github.rtyvz.senla.tr.loginapp.login.ui

import android.app.ProgressDialog
import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.databinding.LoginActivityBinding
import com.github.rtyvz.senla.tr.loginapp.login.entity.UserTokenResponse
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import com.github.rtyvz.senla.tr.loginapp.profile.ui.ProfileActivity
import com.github.rtyvz.senla.tr.loginapp.utils.Result
import com.github.rtyvz.senla.tr.loginapp.utils.getString
import com.github.rtyvz.senla.tr.loginapp.utils.putString
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private val checkEmailRegex =
        "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$".toRegex()
    private lateinit var prefs: SharedPreferences
    private var progress: ProgressDialog? = null
    private lateinit var tokenReceiver: BroadcastReceiver
    private lateinit var profileReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager

    companion object {
        const val BROADCAST_USER_PROFILE = "local:BROADCAST_USER_PROFILE"
        const val PREFS_USER_TOKEN = "USER_TOKEN"
        const val EXTRA_USER_TOKEN = "USER_TOKEN"
        const val EXTRA_USER_PROFILE = "USER_PROFILE"
        const val BROADCAST_USER_TOKEN = "local:BROADCAST_USER_TOKEN"
        private const val SAVED_TOKEN = "SAVED_TOKEN"
        private const val COMMA = ","
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        binding = LoginActivityBinding.inflate(layoutInflater)
        prefs = getSharedPreferences(PREFS_USER_TOKEN, Context.MODE_PRIVATE)
        initProgress()


        val token = prefs.getString(SAVED_TOKEN).toString()

        if (token.isNotBlank() && !App.INSTANCE.state.isTasksRunning) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        } else {
            setContentView(binding.root)

            initTokenReceiver()
            initUserProfileReceiver()
            binding.apply {
                loginButton.setOnClickListener {
                    when {
                        passwordEditText.text.isNullOrBlank() || emailEditText.text.isNullOrBlank() -> {
                            errorTextView.text =
                                getString(R.string.login_activity_error_input_value)
                        }
                        //if string doesn't match with regex
                        !emailEditText.text.toString().matches(checkEmailRegex) -> {
                            errorTextView.text =
                                getString(R.string.login_activity_wrong_email)
                        }
                        else -> {
                            progress?.show()
                            errorTextView.text = EMPTY_STRING
                            //start task
                            App.INSTANCE.state.isTasksRunning = true
                            App.TasksProvider.provideLoginTask().execute(
                                binding.emailEditText.text.toString(),
                                binding.passwordEditText.text.toString()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (App.INSTANCE.state.isTasksRunning) {
            progress?.show()
        }

        registerProfileReceiver()
        registerTokenReceiver()
    }


    private fun initUserProfileReceiver() {
        profileReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                requestResult(
                    intent?.getParcelableExtra(
                        EXTRA_USER_PROFILE
                    )
                )
            }
        }
    }

    private fun registerProfileReceiver() {
        localBroadcastManager.registerReceiver(
            profileReceiver,
            IntentFilter(BROADCAST_USER_PROFILE)
        )
    }

    private fun initTokenReceiver() {
        tokenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val token =
                    intent?.getParcelableExtra<Result<UserTokenResponse>>(EXTRA_USER_TOKEN)
                saveToken(token)
            }
        }
    }

    private fun registerTokenReceiver() {
        localBroadcastManager.registerReceiver(tokenReceiver, IntentFilter(BROADCAST_USER_TOKEN))
    }

    private fun initProgress() {
        progress = ProgressDialog(this).apply {
            setMessage(getString(R.string.login_activity_wait_label))
            setCanceledOnTouchOutside(false)
            create()
        }
    }

    private fun putTokenIntoPrefs(token: String) {
        prefs.putString(SAVED_TOKEN, token)
    }

    private fun saveToken(result: Result<UserTokenResponse>?) {
        when (result) {
            is Result.Success -> {
                App.TasksProvider.provideFetchProfileTask().execute(
                    result.responseBody.token,
                    binding.emailEditText.text.toString()
                )
                putTokenIntoPrefs(result.responseBody.token)
            }
            is Result.Error -> {
                disableProgress()
                binding.errorTextView.text = result.error
            }
        }
    }

    private fun requestResult(response: Result<UserProfileResponse>?) {
        when (response) {
            is Result.Success -> {
                val userInformation = response.responseBody
                writeUserDataToFile(userInformation)
                App.INSTANCE.state.isTasksRunning = false
                startActivity(Intent(this, ProfileActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putParcelable(ProfileActivity.EXTRA_USER_PROFILE, response.responseBody)
                    })
                })
                finish()
            }
            is Result.Error -> {
                disableProgress()
                binding.errorTextView.text = response.error
            }
        }
    }

    private fun writeUserDataToFile(userInformation: UserProfileResponse) {
        App.INSTANCE.getUserInformationFile().bufferedWriter(Charset.defaultCharset())
            .use {
                it.write(
                    prepareInformationForWrite(userInformation).joinToString(
                        separator = COMMA,
                        prefix = EMPTY_STRING,
                        postfix = EMPTY_STRING
                    )
                )
            }
    }

    private fun prepareInformationForWrite(userInformation: UserProfileResponse): List<String> {
        return listOf(
            userInformation.email,
            userInformation.firstName,
            userInformation.lastName,
            userInformation.birthDate.toString(),
            userInformation.notes
        )
    }

    private fun disableProgress() {
        progress?.dismiss()
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(profileReceiver)
        localBroadcastManager.unregisterReceiver(tokenReceiver)
        disableProgress()

        super.onPause()
    }
}