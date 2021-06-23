package com.github.rtyvz.senla.tr.loginapp.login.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.loginapp.App
import com.github.rtyvz.senla.tr.loginapp.R
import com.github.rtyvz.senla.tr.loginapp.databinding.LoginActivityBinding
import com.github.rtyvz.senla.tr.loginapp.login.entity.UserTokenResponse
import com.github.rtyvz.senla.tr.loginapp.profile.entity.UserProfileResponse
import com.github.rtyvz.senla.tr.loginapp.profile.ui.ProfileActivity
import com.github.rtyvz.senla.tr.loginapp.task.FragmentWithTasks
import com.github.rtyvz.senla.tr.loginapp.task.TaskCallbacks
import com.github.rtyvz.senla.tr.loginapp.utils.Result
import com.github.rtyvz.senla.tr.loginapp.utils.getString
import com.github.rtyvz.senla.tr.loginapp.utils.putString
import java.nio.charset.Charset

class LoginActivity : AppCompatActivity(),
    TaskCallbacks {
    private lateinit var binding: LoginActivityBinding
    private val checkEmailRegex =
        "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$".toRegex()
    private lateinit var prefs: SharedPreferences
    private var progress: ProgressDialog? = null
    private var isTaskRunning = false

    companion object {
        const val PREFS_USER_TOKEN = "USER_TOKEN"
        private const val SAVED_TOKEN = "SAVED_TOKEN"
        private const val COMMA = ","
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginActivityBinding.inflate(layoutInflater)
        prefs = getSharedPreferences(PREFS_USER_TOKEN, Context.MODE_PRIVATE)
        val token = prefs.getString(SAVED_TOKEN).toString()

        if (token.isNotBlank()) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        } else {
            setContentView(binding.root)
            initProgress()

            if (isTaskRunning) {
                progress?.show()
            }
            binding.apply {
                loginButton.setOnClickListener {
                    val email = emailEditText.text
                    email?.let {
                        when {
                            passwordEditText.text.isNullOrBlank() || emailEditText.text.isNullOrBlank() -> {
                                errorTextView.text =
                                    getString(R.string.login_activity_error_input_value)
                            }
                            //if string don't match with regex
                            !it.matches(checkEmailRegex) -> {
                                errorTextView.text =
                                    getString(R.string.login_activity_wrong_email)
                            }
                            else -> {
                                isTaskRunning = true
                                if (isTaskRunning) {
                                    progress?.show()
                                }
                                errorTextView.text = EMPTY_STRING
                                //start task
                                var fragment =
                                    supportFragmentManager.findFragmentByTag(FragmentWithTasks.TAG)

                                if (fragment == null) {
                                    fragment = createTaskFragment()
                                    supportFragmentManager.beginTransaction()
                                        .add(
                                            fragment,
                                            FragmentWithTasks.TAG
                                        ).commit()
                                } else {
                                    (fragment as FragmentWithTasks).restartLoginTask(
                                        binding.emailEditText.text.toString(),
                                        binding.passwordEditText.text.toString()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun createTaskFragment() = FragmentWithTasks()
        .apply {
            arguments = Bundle().apply {
                putString(
                    FragmentWithTasks.EXTRA_USER_EMAIL,
                    binding.emailEditText.text.toString()
                )
                putString(
                    FragmentWithTasks.EXTRA_USER_PASSWORD,
                    binding.passwordEditText.text.toString()
                )
            }
        }

    private fun initProgress() {
        progress = ProgressDialog(this).apply {
            setMessage(getString(R.string.login_activity_wait_label))
            setCanceledOnTouchOutside(false)
            create()
        }
    }

    private fun saveToken(token: String) {
        prefs.putString(SAVED_TOKEN, token)
    }

    override fun saveToken(result: Result<UserTokenResponse>) {
        when (result) {
            is Result.Success -> {
                saveToken(result.responseBody.token)
            }
            is Result.Error -> {
                disableProgress()
                binding.errorTextView.text = result.error
            }
        }
    }

    override fun requestResult(response: Result<UserProfileResponse>) {
        when (response) {
            is Result.Success -> {
                val userInformation = response.responseBody
                writeUserDataToFile(userInformation)

                startActivity(Intent(this, ProfileActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putParcelable(ProfileActivity.EXTRA_USER_PROFILE, response.responseBody)
                    })
                })
                disableProgress()
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
        isTaskRunning = false
        progress?.dismiss()
    }

    override fun taskRunningYet() {
        isTaskRunning = true
    }

    override fun onDestroy() {
        progress?.dismiss()

        super.onDestroy()
    }
}