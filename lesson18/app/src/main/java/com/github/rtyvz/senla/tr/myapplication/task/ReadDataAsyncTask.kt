package com.github.rtyvz.senla.tr.myapplication.task

import android.os.AsyncTask
import com.github.rtyvz.senla.tr.myapplication.ListManager

class ReadDataAsyncTask(
    private val listManager: ListManager,
    private val block: (List<String>?) -> Unit
) :
    AsyncTask<Void, List<String>?, List<String>?>() {

    companion object {
        private const val TIME_THREAD_SLEEP = 100L
    }

    override fun doInBackground(vararg params: Void?): List<String>? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            Thread.sleep(TIME_THREAD_SLEEP)
            listManager.getData()?.let {

                if (it.isNotEmpty()) {
                    onProgressUpdate(it)
                }
            }
        }
    }

    override fun onProgressUpdate(vararg values: List<String>?) {
        super.onProgressUpdate(*values)

        values[0]?.let {
            block(it)
        }
    }
}