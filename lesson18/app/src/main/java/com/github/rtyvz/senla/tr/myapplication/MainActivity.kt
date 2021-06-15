package com.github.rtyvz.senla.tr.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.myapplication.tasks.CalculatePrimeNumbersAsyncTask
import com.github.rtyvz.senla.tr.myapplication.tasks.MainAsyncTask
import com.github.rtyvz.senla.tr.myapplication.tasks.ReadDataAsyncTask
import com.github.rtyvz.senla.tr.myapplication.tasks.SleepingAsyncTask
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    @Volatile
    private var isThreadCancel: Boolean = true
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

        binding.apply {
            contentTextView.movementMethod = ScrollingMovementMethod()
            startButton.setOnClickListener {
                startButton.isEnabled = false
                calculateTask =
                    CalculatePrimeNumbersAsyncTask(
                        list,
                        waitObject,
                        lastCalculatedNumber
                    ) {
                        localBroadcastManager
                            .sendBroadcastSync(Intent(BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER).apply {
                                putExtra(EXTRA_LAST_CALCULATED_PRIME_NUMBER, it)
                            })
                    }

                readDataAsyncTask =
                    ReadDataAsyncTask(
                        list
                    ) {
                        it?.let { list ->
                            list.forEach { text ->
                                localBroadcastManager
                                    .sendBroadcastSync(Intent(BROADCAST_SAVED_PRIME_NUMBERS).apply {
                                        putExtra(EXTRA_PRIME_NUMBER, text)
                                    })
                                contentTextView.post {
                                    contentTextView.append(text + LINE_BREAK)
                                }
                            }
                        }
                    }

                sleepingAsyncTask =
                    SleepingAsyncTask(
                        waitObject,
                        list
                    )

                mainTask =
                    MainAsyncTask(
                        list,
                        {
                            startButton.isEnabled = true
                        },
                        {
                            calculateTask?.cancel(isThreadCancel)
                            readDataAsyncTask?.cancel(isThreadCancel)
                            sleepingAsyncTask?.cancel(isThreadCancel)
                        },
                        {
                            localBroadcastManager
                                .sendBroadcastSync(Intent(BROADCAST_SAVED_COUNT).apply {
                                    putExtra(EXTRA_COUNT, it)
                                })
                        },
                        lastCount
                    )

                calculateTask?.executeOnExecutor(executorService)
                readDataAsyncTask?.executeOnExecutor(executorService)
                sleepingAsyncTask?.executeOnExecutor(executorService)
                mainTask?.executeOnExecutor(executorService)
            }
        }
    }

    private fun initDataAfterRotate() {
        App.INSTANCE.state?.let {
            //добавляю 1 потому что эти данные уже по факту были выведены +/-
            lastCalculatedNumber = it.lastPrimeNumber + 1
            lastCount = it.lastCount + 1
            it.listOfData.forEach { listValue ->
                binding.contentTextView.append(listValue + LINE_BREAK)
            }
        }
    }

    override fun onStop() {
        calculateTask?.cancel(isThreadCancel)
        readDataAsyncTask?.cancel(isThreadCancel)
        sleepingAsyncTask?.cancel(isThreadCancel)
        mainTask?.cancel(isThreadCancel)

        super.onStop()
    }
}