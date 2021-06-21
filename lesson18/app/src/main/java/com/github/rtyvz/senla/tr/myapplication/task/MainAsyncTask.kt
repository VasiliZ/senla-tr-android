package com.github.rtyvz.senla.tr.myapplication.task

import android.os.AsyncTask
import com.github.rtyvz.senla.tr.myapplication.ListManager

class MainAsyncTask(
    private val listManager: ListManager,
    private val enableButtonBlock: () -> Unit,
    private val signalToCloseTasks: () -> Unit,
    private val sendCount: (Int) -> Unit,
    count: Int
) :
    AsyncTask<Void, Void, Void>() {
    var localCount = count

    companion object {
        private const val TIME_TO_THREAD_SlEEP = 5000L
        private const val FINISH_CALCULATE_VALUES_CONDITION = 11
    }

    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            Thread.sleep(TIME_TO_THREAD_SlEEP)
            listManager.setData(localCount.toString())

            if (localCount == FINISH_CALCULATE_VALUES_CONDITION) {
                signalToCloseTasks()
                break
            }
            sendCount(localCount)
            localCount++
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        enableButtonBlock()
    }
}