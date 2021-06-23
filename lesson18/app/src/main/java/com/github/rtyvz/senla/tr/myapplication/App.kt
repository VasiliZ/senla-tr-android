package com.github.rtyvz.senla.tr.myapplication

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class App : Application() {
    private var _state: State? = null
    val state: State?
        get() {
            return _state
        }

    companion object {
        lateinit var INSTANCE: App
        private const val DEFAULT_VALUE = 0
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val primeNumberReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _state?.listOfData?.add(
                    intent?.getStringExtra(MainActivity.EXTRA_PRIME_NUMBER).toString()
                )
            }
        }
        localBroadcastManager.registerReceiver(
            primeNumberReceiver, IntentFilter(
                MainActivity.BROADCAST_SAVED_PRIME_NUMBERS
            )
        )

        val countReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _state?.valueOfCount = (
                        intent?.getIntExtra(MainActivity.EXTRA_COUNT, DEFAULT_VALUE)
                            ?: DEFAULT_VALUE
                        )
            }
        }
        localBroadcastManager.registerReceiver(
            countReceiver, IntentFilter(
                MainActivity.BROADCAST_SAVED_COUNT
            )
        )

        val lastPrimeNumberReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                _state?.lastCalculatedNumber = (
                        intent?.getIntExtra(
                            MainActivity.EXTRA_LAST_CALCULATED_PRIME_NUMBER,
                            DEFAULT_VALUE
                        ) ?: DEFAULT_VALUE
                        )
            }
        }
        localBroadcastManager.registerReceiver(
            lastPrimeNumberReceiver, IntentFilter(
                MainActivity.BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER
            )
        )
    }

    fun createState() {
        _state = State()
    }
}