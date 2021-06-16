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
    private val interval = 5000
    private val writeToUiInterval = 100
    private val endOfCount = 10
    private val waitObj = Object()
    private val handler = Handler(Looper.getMainLooper())
    private val timeToSleepCalculate = 400L
    private val startLoopIndex = 2
    private val endOfPrimeNumbersCheck = 500
    private var countForExit = 1
    private val zeroInt = 0
    private val twoInt = 2

    companion object {
        private const val BREAK_LINE = "\n"
        private const val TEXT_YUP = "Yup!"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.treadsContentTextView.movementMethod = ScrollingMovementMethod()
        binding.startButton.setOnClickListener {
            it.isEnabled = false
            val first = Thread(Runnable {
                var numbersForCheck = startLoopIndex
                while (shouldWork) {
                    Thread.sleep(timeToSleepCalculate)
                    var countDividers = zeroInt
                    for (i in startLoopIndex..endOfPrimeNumbersCheck) {
                        if (numbersForCheck % i == zeroInt) {
                            countDividers++
                        }
                    }
                    if (countDividers < twoInt) {
                        synchronized(waitObj) {
                            listManager.setData(numbersForCheck.toString())
                            waitObj.notify()
                        }
                    }
                    numbersForCheck++
                }
            })

            val second = Thread(Runnable {
                while (shouldWork) {
                    Thread.sleep(writeToUiInterval.toLong())
                    handler.post {
                        val newData = listManager.getData()
                        newData?.let { list ->
                            if (list.isNotEmpty()) {
                                list.forEach { listValue ->
                                    binding.treadsContentTextView.append(
                                        listValue + BREAK_LINE
                                    )
                                }
                            }
                        }
                    }
                }
            })

            val fourth = Thread(Runnable {
                while (shouldWork) {
                    synchronized(waitObj) {
                        waitObj.wait()
                        if (shouldWork) {
                            listManager.setData(TEXT_YUP)
                        }
                    }
                }
            })

            val third = Thread(Runnable {
                while (shouldWork) {
                    Thread.sleep(interval.toLong())
                    listManager.setData(countForExit.toString())
                    countForExit++

                    if (countForExit == endOfCount) {
                        shouldWork = false
                        synchronized(waitObj) {
                            waitObj.notify()
                        }
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
}