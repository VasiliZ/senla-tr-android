package com.github.rtyvz.senla.tr.myapplication.tasks

import android.os.AsyncTask
import com.github.rtyvz.senla.tr.myapplication.ListManager

class CalculatePrimeNumbersAsyncTask(
    private val listManager: ListManager,
    private val waitObject: Object,
    private val lastCalculateNumber: Int,
    private val sendLastCalculateNumber: (Int) -> Unit
) :
    AsyncTask<Void, Void, Void>() {
    private val timeToSleepCalculate = 500
    private val startLoopIndex = 2
    private val startCalculatePrimeNumberFrom = lastCalculateNumber

    override fun doInBackground(vararg params: Void?): Void? {
        calculateSimpleDigits()
        return null
    }

    private fun calculateSimpleDigits() {
        for (number in startCalculatePrimeNumberFrom..Int.MAX_VALUE) {
            Thread.sleep(timeToSleepCalculate.toLong())
            var count = 0
            for (i in startLoopIndex..number) {
                if (number % i == 0) {
                    count++
                }
            }

            if (count < 2) {
                synchronized(waitObject) {
                    sendLastCalculateNumber(number)
                    listManager.setData(number.toString())
                    waitObject.notify()
                }
            }
        }
    }
}