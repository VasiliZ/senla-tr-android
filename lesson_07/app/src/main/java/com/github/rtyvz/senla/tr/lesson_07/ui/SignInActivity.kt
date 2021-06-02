package com.github.rtyvz.senla.tr.lesson_07.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.R
import com.github.rtyvz.senla.tr.lesson_07.databinding.SignInActivityBinding
import com.github.rtyvz.senla.tr.lesson_07.entity.UserInformation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: SignInActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.let { binding ->
            binding.singInButton.setOnClickListener {

                val isLoginValid =
                    isValidView(
                        binding.loginEditText,
                        getString(R.string.login_cant_be_empty),
                        binding.loginInputLayout
                    )
                val isPasswordValid =
                    isValidView(
                        binding.passwordEditText,
                        getString(R.string.password_required),
                        binding.passwordInputLayout
                    )

                if (isLoginValid && isPasswordValid) {

                    startActivity(Intent(this, InformationActivity::class.java).also {
                        it.putExtras(
                            Bundle().apply {
                                putParcelable(
                                    InformationActivity.EXTRA_USER_INFORMATION,
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

    private fun isValidView(
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
}