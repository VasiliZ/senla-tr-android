package com.github.rtyvz.senla.tr.myapplication

import android.content.Intent
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
    private val list = ListManager()
    private val waitObject = Object()
    private val executorService = Executors.newFixedThreadPool(4)
    private var lastCalculatedNumber = 2
    private var lastCount = 1
    private var calculateTask: CalculatePrimeNumbersAsyncTask? = null
    private var readDataAsyncTask: ReadDataAsyncTask? = null
    private var sleepingAsyncTask: SleepingAsyncTask? = null
    private var mainTask: MainAsyncTask? = null
    private val localBroadcastManager = LocalBroadcastManager.getInstance(this)

    companion object {
        private const val LINE_BREAK = "\n"
        const val EXTRA_PRIME_NUMBER = "PRIME_NUMBER"
        const val EXTRA_COUNT = "COUNT"
        const val EXTRA_LAST_CALCULATED_PRIME_NUMBER = "LAST_CALCULATED_PRIME_NUMBER"
        const val BROADCAST_SAVED_PRIME_NUMBERS = "local:BROADCAST_SAVED_PRIME_NUMBERS"
        const val BROADCAST_SAVED_COUNT = "local:BROADCAST_SAVED_COUNT"
        const val BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER =
            "local:BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (App.INSTANCE.state == null) {
            App.INSTANCE.createState()
        } else {
            initDataAfterRotate()
        }

        initTasks()

        binding.apply {
            startButton.setOnClickListener {
                startButton.isEnabled = false
                calculateTask?.executeOnExecutor(executorService)
                readDataAsyncTask?.executeOnExecutor(executorService)
                sleepingAsyncTask?.executeOnExecutor(executorService)
                mainTask?.executeOnExecutor(executorService)
            }
        }
    }

    private fun initTasks() {
        calculateTask = initCalculatePrimeNumberTask()
        readDataAsyncTask = initReadDataTask()
        sleepingAsyncTask = initSleepingTask()
        mainTask = initMainTask()
    }

    private fun initCalculatePrimeNumberTask(): CalculatePrimeNumbersAsyncTask {
        return CalculatePrimeNumbersAsyncTask(
            list,
            waitObject,
            lastCalculatedNumber
        ) {
            localBroadcastManager
                .sendBroadcastSync(Intent(BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER).apply {
                    putExtra(EXTRA_LAST_CALCULATED_PRIME_NUMBER, it)
                })
        }
    }

    private fun initReadDataTask(): ReadDataAsyncTask {
        return ReadDataAsyncTask(
            list
        ) {
            it?.let { list ->
                list.forEach { text ->
                    localBroadcastManager
                        .sendBroadcastSync(Intent(BROADCAST_SAVED_PRIME_NUMBERS).apply {
                            putExtra(EXTRA_PRIME_NUMBER, text)
                        })
                    binding.apply {
                        contentTextView.post {
                            contentTextView.apply {
                                append(text + LINE_BREAK)
                                scroll.fullScroll(View.FOCUS_DOWN)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initSleepingTask(): SleepingAsyncTask {
        return SleepingAsyncTask(
            waitObject,
            list
        )
    }

    private fun initMainTask(): MainAsyncTask {
        return MainAsyncTask(
            list,
            {
                binding.startButton.isEnabled = true
            },
            {
                calculateTask?.cancel(shouldThreadCancel)
                readDataAsyncTask?.cancel(shouldThreadCancel)
                sleepingAsyncTask?.cancel(shouldThreadCancel)
            },
            {
                localBroadcastManager
                    .sendBroadcastSync(Intent(BROADCAST_SAVED_COUNT).apply {
                        putExtra(EXTRA_COUNT, it)
                    })
            },
            lastCount
        )
    }

    private fun initDataAfterRotate() {
        App.INSTANCE.state?.let {
            //добавляю 1 потому что эти данные уже по факту были выведены +/-
            lastCalculatedNumber = it.lastCalculatedNumber + 1
            lastCount = it.valueOfCount + 1
            it.listOfData.forEach { listValue ->
                binding.contentTextView.append(listValue + LINE_BREAK)
            }
        }
    }

    override fun onStop() {
        calculateTask?.cancel(shouldThreadCancel)
        readDataAsyncTask?.cancel(shouldThreadCancel)
        sleepingAsyncTask?.cancel(shouldThreadCancel)
        mainTask?.cancel(shouldThreadCancel)

        super.onStop()
    }
}