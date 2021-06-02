package com.github.rtyvz.senla.tr.lesson_07.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val URL = "http://developer.android.com/intl/ru/index.html"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        binding.registrationButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.aboutProgramLabel.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(URL)
            })
        }
    }
}