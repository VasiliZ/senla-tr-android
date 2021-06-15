package com.github.rtyvz.senla.tr.myapplication.tasks

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
    private val millisForThreadSleep = 5000L
    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            Thread.sleep(millisForThreadSleep)
            listManager.setData(localCount.toString())
            if (localCount == 11) {
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