package com.github.rtyvz.senla.tr.lesson_06

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_06.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userLogin = "User"
    private val password = "4"
    private var isLoginError = true
    private var isPasswordError = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loginAction.setOnClickListener {

                checkInputs()
                if (!isLoginError && !isPasswordError) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.login_succes),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            registerAction.setOnClickListener {

                checkInputs()
                if (!isLoginError && !isPasswordError) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.redirect),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun checkInputs() {
        binding.apply {
            when {
                loginInput.text.toString().isBlank() -> {
                    loginInputLayout.error = getString(R.string.login_required)
                }

                loginInput.text.toString() != userLogin -> {
                    loginInputLayout.error = getString(R.string.wrong_login)
                }

                else -> {
                    isLoginError = false
                    loginInputLayout.error = null
                }
            }

            when {
                passwordInput.text.toString().isBlank() -> {
                    passwordInputLayout.error = getString(R.string.password_required)
                }

                passwordInput.text.toString() != password -> {
                    passwordInputLayout.error = getString(R.string.wrong_password)
                }

                else -> {
                    isPasswordError = false
                    passwordInputLayout.error = null
                }
            }
        }
    }
}