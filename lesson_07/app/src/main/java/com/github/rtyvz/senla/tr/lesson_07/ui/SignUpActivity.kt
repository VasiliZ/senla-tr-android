package com.github.rtyvz.senla.tr.lesson_07.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.Const
import com.github.rtyvz.senla.tr.lesson_07.R
import com.github.rtyvz.senla.tr.lesson_07.databinding.SignUpActivityBinding
import com.github.rtyvz.senla.tr.lesson_07.entity.UserInformation

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: SignUpActivityBinding
    private var isValidLogin: Boolean = false
    private var isValidPassword: Boolean = false
    private var isValidRepeatedPassword: Boolean = false
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validateLogin()
        validatePassword()
        validateRepeatedPassword()
        validateSingUpButton()
        validateAgreeCheckBox()

        binding.backButton.setOnClickListener {
            this.finish()
        }

        binding.signUpButton.setOnClickListener {
            val isNameEmpty = isNameEmpty()
            val isSecondNameEmpty = isSecondNameEmpty()
            val isLoginEmpty = isLoginEmpty()
            val isPasswordEmpty = isPasswordEmpty()
            val isRepeatPasswordEmpty = isRepeatPasswordEmpty()

            if (isNameEmpty && isSecondNameEmpty && isLoginEmpty
                && isPasswordEmpty && isRepeatPasswordEmpty
            ) {

                if (isValidLogin && isValidPassword && isValidRepeatedPassword) {
                    startActivity(Intent(this, InformationActivity::class.java).also {
                        it.putExtras(Bundle().also {
                            it.putParcelable(
                                Const.USER_DATA_BUNDLE,
                                UserInformation(
                                    login = binding.loginEditText.text.toString(),
                                    password = binding.passwordEditText.text.toString(),
                                    name = binding.nameEditText.text.toString(),
                                    secondName = binding.secondNameEditText.text.toString(),
                                    sex = resources
                                        .getResourceEntryName(
                                            binding
                                                .sexRadioGroup
                                                .checkedRadioButtonId
                                        ),
                                    additionalInformation = binding
                                        .additionalInformationEditText
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

    private fun isLoginEmpty(): Boolean {
        return if (binding.loginEditText.text.isNullOrBlank()) {
            binding.registrationLoginInputLayout.error = getString(R.string.login_required)
            false
        } else {
            binding.registrationLoginInputLayout.error = null
            true
        }
    }

    private fun isPasswordEmpty(): Boolean {
        return if (binding.passwordEditText.text.isNullOrBlank()) {
            binding.registrationPasswordInputLayout.error = getString(R.string.password_required)
            false
        } else {
            binding.registrationPasswordInputLayout.error = null
            true
        }
    }

    private fun isRepeatPasswordEmpty(): Boolean {
        return if (binding.passwordAgainEditText.text.isNullOrBlank()) {
            binding.registrationPasswordAgainInputLayout.error =
                getString(R.string.password_required)
            false
        } else {
            binding.registrationPasswordAgainInputLayout.error = null
            true
        }
    }

    private fun validateSingUpButton() {
        binding.signUpButton.isEnabled = binding.iAgreeCheckBox.isChecked
    }

    private fun validateLogin() {
        binding.loginEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length < 4) {
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

    private fun validatePassword() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length < 8) {
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

    private fun validateRepeatedPassword() {
        binding.passwordAgainEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length < 8) {
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

    private fun isNameEmpty(): Boolean {
        return if (binding.nameEditText.text.isNullOrBlank()) {
            binding.nameInputLayout.error = getString(R.string.name_required)
            false
        } else {
            binding.nameInputLayout.error = null
            true
        }
    }

    private fun isSecondNameEmpty(): Boolean {
        return if (binding.secondNameEditText.text.isNullOrBlank()) {
            binding.secondNameInputLayout.error = getString(R.string.name_required)
            false
        } else {
            binding.secondNameInputLayout.error = null
            true
        }
    }

    private fun validateAgreeCheckBox() {
        binding.iAgreeCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.signUpButton.isEnabled = isChecked
        }
    }
}