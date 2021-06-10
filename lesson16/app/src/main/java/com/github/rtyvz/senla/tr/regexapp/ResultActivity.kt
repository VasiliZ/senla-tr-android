package com.github.rtyvz.senla.tr.regexapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.regexapp.databinding.ResultActivityBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ResultActivityBinding

    companion object {
        const val EXTRA_PARSE_RESULT = "PARSE_RESULT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ResultActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.let {
            binding.parseResult.text = it.getStringExtra(EXTRA_PARSE_RESULT)
        }
    }
}