package com.github.rtyvz.senla.tr.lesson_07

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson_07.databinding.SignUpActivityBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: SignUpActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}