package com.github.rtyvZ.senla.tr.multithreadapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvZ.senla.tr.multithreadapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    @Volatile
    private var shouldWork = true
    private lateinit var binding: ActivityMainBinding
    private val listManager = ListManager()
    private val endOfCount = 10
    private val waitObj = Object()
    private val handler = Handler(Looper.getMainLooper())
    private val startLoopIndex = 2
    private val zeroInt = 0
    private val twoInt = 2

    companion object {
        private const val BREAK_LINE = "\n"
        private const val TEXT_YUP = "Yup!"
        private const val LONG_SLEEP_INTERVAL = 5000L
        private const val WRITE_TO_UI_INTERVAL = 100L
        private const val TIME_TO_SLEEP_CALCULATE = 400L
        private const val END_OF_PRIME_NUMBER_CHECK = 500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.treadsContentTextView.movementMethod = ScrollingMovementMethod()
        binding.startButton.setOnClickListener {
            it.isEnabled = false
            binding.treadsContentTextView.text = ""
            val secondThread = Thread(Runnable {
                var nextPrimeNumber = startLoopIndex
                while (shouldWork) {
                    Thread.sleep(TIME_TO_SLEEP_CALCULATE)
                    var dividerCount = zeroInt
                    for (i in nextPrimeNumber..END_OF_PRIME_NUMBER_CHECK) {
                        if (nextPrimeNumber % i == zeroInt) {
                            dividerCount++
                        }
                    }
                    if (dividerCount < twoInt) {
                        synchronized(waitObj) {
                            listManager.setData(nextPrimeNumber.toString())
                            waitObj.notify()
                        }
                    }
                    nextPrimeNumber++
                }
            })

            val firstThread = Thread(Runnable {
                while (shouldWork) {
                    Thread.sleep(WRITE_TO_UI_INTERVAL)
                    handler.post {
                        listManager.getData()?.forEach { listValue ->
                            binding.treadsContentTextView.append(
                                listValue + BREAK_LINE
                            )
                        }
                    }
                }
            })

            val fourth = Thread(Runnable {
                synchronized(waitObj) {
                    while (shouldWork) {
                        waitObj.wait()
                        listManager.setData(TEXT_YUP)
                    }
                }
            })

            val third = Thread(Runnable {
                var countForExit = 1
                while (shouldWork) {
                    Thread.sleep(LONG_SLEEP_INTERVAL)
                    listManager.setData(countForExit.toString())
                    countForExit++

                    if (countForExit == endOfCount) {
                        shouldWork = false

                        synchronized(waitObj) {
                            waitObj.notify()
                        }
                        secondThread.join()
                        firstThread.join()
                        fourth.join()

                        handler.post {
                            binding.startButton.isEnabled = true
                        }
                    }
                }
            })

            secondThread.start()
            firstThread.start()
            third.start()
            fourth.start()
        }
    }
}