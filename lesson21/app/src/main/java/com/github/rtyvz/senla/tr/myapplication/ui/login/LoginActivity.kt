package com.github.rtyvz.senla.tr.myapplication.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.myapplication.R
import com.github.rtyvz.senla.tr.myapplication.common.ActivityCallBacks
import com.github.rtyvz.senla.tr.myapplication.common.BoltsFragment
import com.github.rtyvz.senla.tr.myapplication.databinding.LoginActivityBinding
import com.github.rtyvz.senla.tr.myapplication.models.UserProfileEntity
import com.github.rtyvz.senla.tr.myapplication.ui.profile.ProfileActivity
import com.github.rtyvz.senla.tr.myapplication.utils.Result
import com.github.rtyvz.senla.tr.myapplication.utils.putString

class LoginActivity : AppCompatActivity(),
    ActivityCallBacks {
    private lateinit var binding: LoginActivityBinding
    private val regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z-.]+\$".toRegex()
    private lateinit var progressDialog: ProgressDialog
    private lateinit var prefs: SharedPreferences

    companion object {
        private const val EMPTY_STRING = ""
        const val PREFS_USER_TOKEN = "USER_TOKEN"
        const val SAVE_TOKEN = "TOKEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initProgress()
        prefs = getSharedPreferences(PREFS_USER_TOKEN, Context.MODE_PRIVATE)
        binding.apply {
            loginButton.setOnClickListener {
                when {
                    userEmailEditText.text.isNullOrBlank() || userPasswordEditText.text.isNullOrBlank() ->
                        errorTextView.text =
                            getString(R.string.login_activity_fields_is_empty_error)
                    !userEmailEditText.text.toString().matches(regexEmail) -> {
                        errorTextView.text = getString(R.string.login_activity_it_isnt_email_error)
                    }
                    else -> {
                        errorTextView.text =
                            EMPTY_STRING

                        supportFragmentManager.beginTransaction()
                            .add(
                                BoltsFragment()
                                    .apply {
                                        arguments = Bundle().apply {
                                            putString(
                                                BoltsFragment.EXTRA_USER_EMAIL,
                                                userEmailEditText.text.toString()
                                            )
                                            putString(
                                                BoltsFragment.EXTRA_USER_PASSWORD,
                                                userPasswordEditText.text.toString()
                                            )
                                        }
                                    },
                                BoltsFragment.TAG
                            ).commit()
                    }
                }
            }
        }
    }

    private fun initProgress() {
        progressDialog = ProgressDialog(this).apply {
            setMessage(getString(R.string.login_activity_wait_label))
            setCanceledOnTouchOutside(false)
            create()
        }
    }

    override fun saveToken(token: String) {
        prefs.putString(SAVE_TOKEN, token)
    }

    override fun error(errorMsg: String) {
        binding.errorTextView.text = errorMsg
    }

    override fun saveUserProfile(result: Result<UserProfileEntity>) {
        when (result) {
            is Result.Success -> {
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
}