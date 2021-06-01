package com.github.rtyvz.senla.tr.lesson_07

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.databinding.SignInActivityBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: SignInActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignInActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}