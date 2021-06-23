package com.github.rtyvz.senla.tr.myapplication.task

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.MainActivity

class MainAsyncTask(
    private val listData: MutableList<String>,
    private val enableButtonBlock: () -> Unit,
    private val signalToCloseTasks: () -> Unit,
    private val localBroadcastManager: LocalBroadcastManager,
    count: Int
) : AsyncTask<Void, Void, Void>() {
    var localCount = count

    companion object {
        private const val TIME_TO_THREAD_SlEEP = 5000L
        private const val FINISH_CALCULATE_VALUES_CONDITION = 11
    }

    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            Thread.sleep(TIME_TO_THREAD_SlEEP)
            synchronized(listData) {
                listData.add(localCount.toString())
            }

            if (localCount == FINISH_CALCULATE_VALUES_CONDITION) {
                signalToCloseTasks()
                break
            }

            localBroadcastManager
                .sendBroadcastSync(Intent(MainActivity.BROADCAST_SAVED_COUNT).apply {
                    putExtra(MainActivity.EXTRA_COUNT, localCount)
                })
            localCount++
        }

        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)

        enableButtonBlock()
    }
}