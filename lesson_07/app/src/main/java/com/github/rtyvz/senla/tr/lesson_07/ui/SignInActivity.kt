package com.github.rtyvz.senla.tr.lesson_07.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.Const
import com.github.rtyvz.senla.tr.lesson_07.R
import com.github.rtyvz.senla.tr.lesson_07.databinding.SignInActivityBinding
import com.github.rtyvz.senla.tr.lesson_07.entity.UserInformation

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: SignInActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.let { binding ->
            binding.singInButton.setOnClickListener {
                val isLoginValid = isLoginInputValid(binding.loginEditText.text.toString())
                val isPasswordValid = isPasswordInputValid(binding.passwordEditText.text.toString())
                if (isLoginValid && isPasswordValid) {
                    startActivity(Intent(this, InformationActivity::class.java).also {
                        it.putExtras(
                            Bundle().also { bundle ->
                                bundle.putParcelable(
                                    Const.USER_DATA_BUNDLE,
                                    UserInformation(
                                        login = binding.loginEditText.text.toString(),
                                        password = binding.passwordEditText.text.toString()
                                    )
                                )
                            }
                        )
                    })
                }
            }

            binding.backButton.setOnClickListener {
                this.finish()
            }
        }
    }

    private fun isLoginInputValid(value: String): Boolean {
        return if (value.isBlank()) {
            binding.loginInputLayout.error = getString(R.string.login_cant_be_empty)
            false
        } else {
            binding.loginInputLayout.error = null
            true
        }
    }

    private fun isPasswordInputValid(value: String): Boolean {
        return if (value.isBlank()) {
            binding.passwordInputLayout.error = getString(R.string.password_required)
            false
        } else {
            binding.passwordInputLayout.error = null
            true
        }
    }
}