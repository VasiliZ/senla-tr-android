package com.github.rtyvz.senla.tr.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.myapplication.task.CalculatePrimeNumbersAsyncTask
import com.github.rtyvz.senla.tr.myapplication.task.MainAsyncTask
import com.github.rtyvz.senla.tr.myapplication.task.ReadDataAsyncTask
import com.github.rtyvz.senla.tr.myapplication.task.SleepingAsyncTask
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    @Volatile
    private var shouldThreadCancel: Boolean = true
    private lateinit var binding: ActivityMainBinding
    private val waitObject = Object()
    private val executorService = Executors.newFixedThreadPool(4)
    private var lastCalculatedNumber = 2
    private var lastCount = 1
    private lateinit var state: State
    private lateinit var calculateTask: CalculatePrimeNumbersAsyncTask
    private lateinit var readDataAsyncTask: ReadDataAsyncTask
    private lateinit var sleepingAsyncTask: SleepingAsyncTask
    private lateinit var mainTask: MainAsyncTask
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var primeNumberReceiver: BroadcastReceiver
    private lateinit var countReceiver: BroadcastReceiver
    private lateinit var lastPrimeNumberReceiver: BroadcastReceiver
    private lateinit var readDataReceiver: BroadcastReceiver
    private lateinit var writeYupReceiver: BroadcastReceiver

    companion object {
        private const val LINE_BREAK = "\n"
        const val EXTRA_PRIME_NUMBER = "PRIME_NUMBER"
        const val EXTRA_COUNT = "COUNT"
        const val EXTRA_LAST_CALCULATED_PRIME_NUMBER = "LAST_CALCULATED_PRIME_NUMBER"
        const val EXTRA_READ_DATA = "READ_DATA"
        const val EXTRA_YUP = "YUP"
        const val BROADCAST_SEND_YUP = "local:BROADCAST_SEND_YUP"
        const val BROADCAST_SAVED_PRIME_NUMBERS = "local:BROADCAST_SAVED_PRIME_NUMBERS"
        const val BROADCAST_SAVED_COUNT = "local:BROADCAST_SAVED_COUNT"
        const val BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER =
            "local:BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER"
        const val BROADCAST_READ_DATA = "local:BROADCAST_READ_DATA"
        private const val DEFAULT_VALUE = 0
        private const val EMPTY_STRING = ""
    }

    override fun onResume() {
        super.onResume()

        initCountReceiver()
        initPrimeNumberReceiver()
        initLastPrimeNumberReceiver()
        initReadDataReceiver()
        initWriteYupReceiver()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        App.INSTANCE.state = State()
        App.INSTANCE.state?.let {
            state = it
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        if (state.printedValues.isNotBlank()) {
            binding.contentTextView.text = state.printedValues
            lastCount = state.valueOfCount
            lastCalculatedNumber = state.lastCalculatedNumber
        }

        initTasks()

        binding.apply {
            startButton.setOnClickListener {
                startButton.isEnabled = false
                calculateTask.executeOnExecutor(executorService)
                readDataAsyncTask.executeOnExecutor(executorService)
                sleepingAsyncTask.executeOnExecutor(executorService)
                mainTask.executeOnExecutor(executorService)
            }
        }
    }

    private fun initPrimeNumberReceiver() {
        primeNumberReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                synchronized(state.listOfData) {
                    state.listOfData.add(
                        intent?.extras?.get(EXTRA_PRIME_NUMBER).toString()
                    )
                }
            }
        }
        localBroadcastManager.registerReceiver(
            primeNumberReceiver, IntentFilter(
                BROADCAST_SAVED_PRIME_NUMBERS
            )
        )
    }

    private fun initCountReceiver() {
        countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                state.valueOfCount =
                    intent?.getIntExtra(EXTRA_COUNT, DEFAULT_VALUE)
                        ?: DEFAULT_VALUE

            }
        }
        localBroadcastManager.registerReceiver(
            countReceiver, IntentFilter(
                BROADCAST_SAVED_COUNT
            )
        )
    }

    private fun initLastPrimeNumberReceiver() {
        lastPrimeNumberReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                state.lastCalculatedNumber =
                    intent?.getIntExtra(
                        EXTRA_LAST_CALCULATED_PRIME_NUMBER,
                        DEFAULT_VALUE
                    ) ?: DEFAULT_VALUE
            }
        }
        localBroadcastManager.registerReceiver(
            lastPrimeNumberReceiver, IntentFilter(
                BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER
            )
        )
    }

    private fun initReadDataReceiver() {
        readDataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                synchronized(state.listOfData) {
                    intent?.getStringArrayListExtra(EXTRA_READ_DATA)?.forEach {
                        binding.apply {
                            contentTextView.post {
                                contentTextView.apply {
                                    append("$it$LINE_BREAK")
                                    scroll.fullScroll(View.FOCUS_DOWN)
                                }
                            }
                        }
                    }
                    state.listOfData.clear()
                }
            }
        }
        localBroadcastManager.registerReceiver(
            readDataReceiver, IntentFilter(
                BROADCAST_READ_DATA
            )
        )
    }

    private fun initWriteYupReceiver() {
        writeYupReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val yupValue = intent?.extras?.getString(
                    EXTRA_YUP
                ) ?: EMPTY_STRING

                synchronized(state.listOfData) {
                    state.listOfData.add(
                        yupValue
                    )
                }
            }
        }
        localBroadcastManager.registerReceiver(
            writeYupReceiver, IntentFilter(BROADCAST_SEND_YUP)
        )
    }

    private fun initTasks() {
        calculateTask = initCalculatePrimeNumberTask()
        readDataAsyncTask = initReadDataTask()
        sleepingAsyncTask = initSleepingTask()
        mainTask = initMainTask()
    }

    private fun initCalculatePrimeNumberTask(): CalculatePrimeNumbersAsyncTask {
        return CalculatePrimeNumbersAsyncTask(
            waitObject,
            lastCalculatedNumber
        )
    }

    private fun initReadDataTask(): ReadDataAsyncTask {
        return ReadDataAsyncTask(
            state.listOfData
        )
    }

    private fun initSleepingTask(): SleepingAsyncTask {
        return SleepingAsyncTask(
            waitObject
        )
    }

    private fun initMainTask(): MainAsyncTask {
        return MainAsyncTask(
            state.listOfData,
            {
                binding.startButton.isEnabled = true
            },
            {
                calculateTask.cancel(shouldThreadCancel)
                readDataAsyncTask.cancel(shouldThreadCancel)
                sleepingAsyncTask.cancel(shouldThreadCancel)
            },
            lastCount
        )
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(primeNumberReceiver)
        localBroadcastManager.unregisterReceiver(countReceiver)
        localBroadcastManager.unregisterReceiver(lastPrimeNumberReceiver)
        localBroadcastManager.unregisterReceiver(lastPrimeNumberReceiver)
        localBroadcastManager.unregisterReceiver(writeYupReceiver)
        state.printedValues = binding.contentTextView.text.toString()

        super.onPause()
    }
}