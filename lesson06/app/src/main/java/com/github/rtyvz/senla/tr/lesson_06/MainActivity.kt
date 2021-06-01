package com.github.rtyvz.senla.tr.lesson_06

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_06.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userLogin = "User"
    private val password = "!234!"
    private var isLoginError = false
    private var isPasswordError = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            loginAction.setOnClickListener {
                if (passwordInput.text.isNullOrBlank()) {
                    isPasswordError = true
                    passwordInputLayout.error = getString(R.string.password_required)
                } else {
                    isPasswordError = false
                    passwordInputLayout.error = null
                }

                if (loginInput.text.isNullOrBlank()) {
                    isLoginError = true
                    loginInputLayout.error = getString(R.string.login_required)
                } else {
                    isLoginError = false
                    loginInputLayout.error = null
                }

                if (loginInput.text.toString() != userLogin) {
                    isLoginError = true
                    loginInputLayout.error = getString(R.string.wrong_login)
                } else {
                    isLoginError = false
                    loginInputLayout.error = null
                }

                if (passwordInput.text.toString() != password) {
                    isPasswordError = true
                    passwordInputLayout.error = getString(R.string.wrong_login)
                } else {
                    isPasswordError = false
                    passwordInputLayout.error = null
                }

                if (!isLoginError && !isPasswordError) {
                    Toast.makeText(this@MainActivity, getString(R.string.login_succes), Toast.LENGTH_SHORT).show()
                }
            }

            registerAction.setOnClickListener {
                if (passwordInput.text.isNullOrBlank()) {
                    isPasswordError = true
                    passwordInputLayout.error = getString(R.string.password_required)
                } else {
                    isPasswordError = false
                    passwordInputLayout.error = null
                }

                if (loginInput.text.isNullOrBlank()) {
                    isLoginError = true
                    loginInputLayout.error = getString(R.string.login_required)
                } else {
                    isLoginError = false
                    loginInputLayout.error = null
                }

                if (!isLoginError && !isPasswordError) {
                    Toast.makeText(this@MainActivity, getString(R.string.login_succes), Toast.LENGTH_SHORT).show()
                }

                if (!isLoginError && !isPasswordError) {
                    Toast.makeText(this@MainActivity, getString(R.string.redirect), Toast.LENGTH_LONG).show()
                    isPasswordError = false
                    isLoginError = false
                }
            }
        }
    }
}