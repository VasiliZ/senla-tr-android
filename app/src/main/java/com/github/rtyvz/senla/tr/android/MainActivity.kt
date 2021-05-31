package com.github.rtyvz.senla.tr.android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        merge(setOf(1, 2, 3), setOf(45, 77, 88))
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
        var sum = 0
        for (i in 0 until 1000) {
            when {
                i % 3 == 0 || i % 5 == 0 -> {
                    sum += i
                }
            }
        }

        Log.d(this.javaClass.canonicalName, sum.toString())
    }

    private fun merge(firstSortedSet: Set<Int>, secondSortedSet: Set<Int>): Set<Int> {
        return firstSortedSet + secondSortedSet
    }
}