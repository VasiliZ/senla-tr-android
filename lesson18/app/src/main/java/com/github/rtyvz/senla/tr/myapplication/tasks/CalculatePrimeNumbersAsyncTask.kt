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
    private val timeToSleepCalculate = 500L
    private val endOfPrimeNumbersCheck = 500
    private val startLoopIndex = 2
    private val twoInt = 2
    private val zeroInt = 0

    override fun doInBackground(vararg params: Void?): Void? {
        var numbersForCheck = lastCalculateNumber
        while (true) {
            Thread.sleep(timeToSleepCalculate)
            var countDividers = zeroInt

            for (i in startLoopIndex..endOfPrimeNumbersCheck) {
                if (numbersForCheck % i == zeroInt) {
                    countDividers++
                    sendLastCalculateNumber(numbersForCheck)
                }
            }

            if (countDividers < twoInt) {
                synchronized(waitObject) {
                    listManager.setData(numbersForCheck.toString())
                    waitObject.notify()
                }
            }
            numbersForCheck++
        }
    }
}