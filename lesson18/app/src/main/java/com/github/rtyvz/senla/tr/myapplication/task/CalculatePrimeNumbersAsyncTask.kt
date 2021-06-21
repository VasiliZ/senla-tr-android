package com.github.rtyvz.senla.tr.myapplication.task

import android.os.AsyncTask
import com.github.rtyvz.senla.tr.myapplication.ListManager

class CalculatePrimeNumbersAsyncTask(
    private val listManager: ListManager,
    private val waitObject: Object,
    private val lastCalculateNumber: Int,
    private val sendLastCalculateNumber: (Int) -> Unit
) :
    AsyncTask<Void, Void, Void>() {

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
                    sendLastCalculateNumber(numbersForCheck)
                }
            }

            if (countDividers < MAX_NUMBER_DIVIDERS) {
                synchronized(waitObject) {
                    listManager.setData(numbersForCheck.toString())
                    waitObject.notify()
                }
            }
            numbersForCheck++
        }
    }
}