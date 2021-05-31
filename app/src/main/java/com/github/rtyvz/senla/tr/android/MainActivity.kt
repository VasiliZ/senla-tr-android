package com.github.rtyvz.senla.tr.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in 1..100) {
            when {
                i % 3 == 0 && i % 5 == 0 && i % 7 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Fizz Buzz Woof")
                }
                i % 3 == 0 && i % 5 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Fizz Buzz")
                }
                i % 3 == 0 && i % 7 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Fizz Woof")
                }
                i % 5 == 0 && i % 7 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Buzz Woof")
                }
                i % 3 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Fizz")
                }
                i % 5 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Fizz")
                }
                i % 7 == 0 -> {
                    Log.d(this.javaClass.canonicalName, "Woof")
                }
                else -> {
                    Log.d(this.javaClass.canonicalName, i.toString())
                }
            }
        }
    }
}