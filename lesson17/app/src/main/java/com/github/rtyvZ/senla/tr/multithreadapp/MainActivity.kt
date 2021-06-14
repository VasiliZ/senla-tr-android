package com.github.rtyvZ.senla.tr.multithreadapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvZ.senla.tr.multithreadapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Volatile
    private var shouldExit = true
    private val listManager = ListManager()
    private val interval = 5000
    private val writeToUiInterval = 100
    private val waitObj = Object()
    private var count = 1
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            it.isEnabled = false
            val first = Thread(Runnable {
                try {
                    calculateSimpleDigits()
                } catch (e: InterruptedException) {
                    return@Runnable
                }
            })
            val second = Thread(Runnable {
                while (shouldExit) {
                    Thread.sleep(writeToUiInterval.toLong())
                    handler.post {
                        Log.d("count", "send on UI")
                        binding.treadsContent.append(
                            listManager.getData()
                                ?.joinToString(separator = "\n", prefix = " ", postfix = "")
                        )
                    }
                }
            })

            val fourth = Thread {
                while (shouldExit) {
                    synchronized(waitObj) {
                        waitObj.wait()
                        listManager.setData("Yup!")
                    }
                }
            }

            val third = Thread(Runnable {
                while (shouldExit) {
                    Thread.sleep(interval.toLong())
                    listManager.setData(count.toString())
                    count++
                    Log.d("count", count.toString())
                    if (count == 3) {
                        shouldExit = false
                        first.interrupt()
                        first.join()
                        second.join()
                        fourth.join()
                        handler.post {
                            binding.startButton.isEnabled = true
                        }
                    }
                }
            })

            first.start()
            second.start()
            third.start()
            fourth.start()
        }
    }

    private fun calculateSimpleDigits() {
        for (number in 2..1000) {
            Thread.sleep(1000)
            var count = 0
            for (i in 2..number) {
                if (number % i == 0) {
                    count++
                }
            }

            if (count < 2) {
                synchronized(waitObj) {
                    listManager.setData(number.toString())
                    waitObj.notify()
                }
            }
        }
    }
}