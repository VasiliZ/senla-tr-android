package com.github.rtyvz.senla.tr.lesson05

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.lesson05.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            calculateAction.setOnClickListener {

                val firstValue = firstNumber.text.toString()
                val secondValue = secondNumber.text.toString()

                if (firstValue.isBlank() || secondValue.isBlank()) {
                    result.text = getString(R.string.input_error)
                } else {
                    result.text = (firstValue.toDouble() + secondValue.toDouble()).toString()
                }
            }
        }
    }
}