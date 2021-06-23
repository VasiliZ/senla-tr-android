package com.github.rtyvz.senla.tr.myapplication.task

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.MainActivity

class CalculatePrimeNumbersAsyncTask(
    private val localBroadcastManager: LocalBroadcastManager,
    private val waitObject: Object,
    private val lastCalculateNumber: Int
) : AsyncTask<Void, Void, Void>() {

    companion object {
        private const val TIME_FOR_CALCULATE_THREAD_SLEEP = 500L
        private const val LAST_NUMBER_FOR_CHECK = 500
        private const val START_LOOP_INDEX = 2
        private const val MAX_NUMBER_DIVIDERS = 2
        private const val INITIAL_VALUE = 0
    }

    override fun doInBackground(vararg params: Void?): Void? {
        var numbersForCheck = lastCalculateNumber
        while (true) {
            Thread.sleep(TIME_FOR_CALCULATE_THREAD_SLEEP)
            var countDividers = INITIAL_VALUE

            for (i in START_LOOP_INDEX..LAST_NUMBER_FOR_CHECK) {
                if (numbersForCheck % i == INITIAL_VALUE) {
                    countDividers++
                }
            }

            if (countDividers < MAX_NUMBER_DIVIDERS) {
                localBroadcastManager.sendBroadcast(Intent(MainActivity.BROADCAST_SAVED_PRIME_NUMBERS).apply {
                    putExtra(MainActivity.EXTRA_PRIME_NUMBER, numbersForCheck)
                })
                localBroadcastManager.sendBroadcast(Intent(MainActivity.BROADCAST_SAVED_LAST_CALCULATED_PRIME_NUMBER).apply {
                    putExtra(MainActivity.EXTRA_LAST_CALCULATED_PRIME_NUMBER, numbersForCheck)
                })
                synchronized(waitObject) {
                    waitObject.notify()
                }
            }
            numbersForCheck++
        }
    }
}