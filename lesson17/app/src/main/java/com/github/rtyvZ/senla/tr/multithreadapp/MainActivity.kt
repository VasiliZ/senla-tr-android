package com.github.rtyvZ.senla.tr.multithreadapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvZ.senla.tr.multithreadapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @Volatile
    private var shouldExit = true
    private lateinit var binding: ActivityMainBinding
    private val listManager = ListManager()
    private val interval = 5000
    private val writeToUiInterval = 100
    private val endOfCount = 10
    private val waitObj = Object()
    private val handler = Handler(Looper.getMainLooper())
    private val timeToSleepCalculate = 500
    private val startLoopIndex = 2

    companion object {
        private const val EMPTY_STRING = ""
        private const val TEXT_YUP = "Yup!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.treadsContentTextView.movementMethod = ScrollingMovementMethod()
        binding.startButton.setOnClickListener {
            var count = 1
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
                        val newData = listManager.getData()
                        newData?.let { list ->
                            if (list.isNotEmpty()) {
                                list.forEach { listValue ->
                                    binding.treadsContentTextView.append(
                                        listValue + "\n"
                                    )
                                }
                            }
                        }
                    }
                }
            })

            val fourth = Thread(Runnable {
                while (shouldExit) {
                    synchronized(waitObj) {
                        waitObj.wait()
                        listManager.setData(TEXT_YUP)
                    }
                }
            })

            val third = Thread(Runnable {
                while (shouldExit) {
                    Thread.sleep(interval.toLong())
                    listManager.setData(count.toString())
                    count++
                    if (count == endOfCount) {
                        shouldExit = false
                        fourth.join()
                        first.interrupt()
                        first.join()
                        second.join()
                        handler.post {
                            binding.startButton.isEnabled = true
                        }
                        break
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
        for (number in startLoopIndex..Int.MAX_VALUE) {
            Thread.sleep(timeToSleepCalculate.toLong())
            var count = 0
            for (i in startLoopIndex..number) {
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