package com.github.rtyvz.senla.tr.myapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.myapplication.databinding.LoginActivityBinding
import com.github.rtyvz.senla.tr.myapplication.models.UserProfile

class LoginActivity : AppCompatActivity(), ActivityCallBacks {
    private lateinit var binding: LoginActivityBinding
    private val regexEmail = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z-.]+\$".toRegex()
    private lateinit var progressDialog: ProgressDialog
    private var isTaskRunning = true

    companion object {
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initProgress()

        if (isTaskRunning) {
            progressDialog.show()
        }

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
                        errorTextView.text = EMPTY_STRING

                        supportFragmentManager.beginTransaction()
                            .add(BoltsFragment().apply {
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
                            }, BoltsFragment.TAG).commit()
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
        Log.d("token", token)
    }

    override fun error(errorMsg: String) {
        binding.errorTextView.text = errorMsg
    }

    override fun saveUserProfile(result: Result<UserProfile>) {
        when (result) {
            is Result.Success -> {
                Log.d("profile", result.responseBody.toString())
            }
            is Result.Error -> binding.errorTextView.text = result.error
        }
    }
}