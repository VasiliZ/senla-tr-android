package com.github.rtyvz.senla.tr.lesson_07.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.R
import com.github.rtyvz.senla.tr.lesson_07.databinding.SignUpActivityBinding
import com.github.rtyvz.senla.tr.lesson_07.entity.UserInformation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: SignUpActivityBinding
    private var isValidLogin: Boolean = false
    private var isValidPassword: Boolean = false
    private var isValidRepeatedPassword: Boolean = false
    private var password = ""

    companion object {
        private const val MIN_LOGIN_LENGTH = 4
        private const val MIN_PASSWORD_LENGTH = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputLoginTextListener()
        inputPasswordTextListener()
        inputRepeatPasswordListener()
        validateSingUpButton()
        validateAgreeCheckBox()

        binding.apply {
            backButton.setOnClickListener {
                finish()
            }

            signUpButton.setOnClickListener {

                val isNameEmpty = isValueViewEmpty(
                    nameEditText,
                    getString(R.string.name_required),
                    nameInputLayout
                )
                val isSecondNameEmpty = isValueViewEmpty(
                    secondNameEditText,
                    getString(R.string.name_required),
                    secondNameInputLayout
                )
                val isLoginEmpty = isValueViewEmpty(
                    loginEditText,
                    getString(R.string.login_required),
                    registrationLoginInputLayout
                )
                val isPasswordEmpty = isValueViewEmpty(
                    passwordEditText,
                    getString(R.string.password_required),
                    registrationPasswordInputLayout
                )
                val isRepeatPasswordEmpty = isValueViewEmpty(
                    passwordAgainEditText,
                    getString(R.string.password_required),
                    registrationPasswordAgainInputLayout
                )

                if (isNameEmpty && isSecondNameEmpty && isLoginEmpty
                    && isPasswordEmpty && isRepeatPasswordEmpty
                ) {

                    if (isValidLogin && isValidPassword && isValidRepeatedPassword) {
                        startActivity(
                            Intent(
                                this@SignUpActivity,
                                InformationActivity::class.java
                            ).also {
                                it.putExtras(Bundle().apply {
                                    putParcelable(
                                        InformationActivity.EXTRA_USER_INFORMATION,
                                        UserInformation(
                                            login = loginEditText.text.toString(),
                                            password = passwordEditText.text.toString(),
                                            name = nameEditText.text.toString(),
                                            secondName = secondNameEditText.text.toString(),
                                            sex = resources
                                                .getResourceEntryName(
                                                    sexRadioGroup
                                                        .checkedRadioButtonId
                                                ),
                                            additionalInformation = additionalInformationEditText
                                                .text
                                                .toString()
                                        )
                                    )
                                })
                            })
                    }
                }
            }
        }
    }

    private fun isValueViewEmpty(
        view: TextInputEditText,
        error: String,
        layout: TextInputLayout
    ): Boolean {
        return if (view.text.isNullOrBlank()) {
            layout.error = error
            false
        } else {
            layout.error = null
            true
        }
    }

    private fun validateSingUpButton() {
        binding.signUpButton.isEnabled = binding.iAgreeCheckBox.isChecked
    }

    private fun inputLoginTextListener() {
        binding.loginEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length < MIN_LOGIN_LENGTH) {
                        binding.registrationLoginInputLayout.error =
                            getString(R.string.login_must_be_longer)
                        isValidLogin = false
                    } else {
                        binding.registrationLoginInputLayout.error = null
                        isValidLogin = true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun inputPasswordTextListener() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length < MIN_PASSWORD_LENGTH) {
                        binding.registrationPasswordInputLayout.error =
                            getString(R.string.password_must_be_longer)
                        isValidPassword = false
                    } else {
                        password = s.toString()
                        binding.registrationPasswordInputLayout.error = null
                        isValidPassword = true
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun inputRepeatPasswordListener() {
        binding.passwordAgainEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length < MIN_PASSWORD_LENGTH) {
                        binding.registrationPasswordAgainInputLayout.error =
                            getString(R.string.password_must_be_longer)
                        isValidRepeatedPassword = false
                    } else {
                        if (password != s.toString()) {
                            binding.registrationPasswordAgainInputLayout.error =
                                getString(R.string.passwords_do_not_much)
                        } else {
                            binding.registrationPasswordAgainInputLayout.error = null
                            isValidRepeatedPassword = true
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun validateAgreeCheckBox() {
        binding.iAgreeCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.signUpButton.isEnabled = isChecked
        }
    }
}