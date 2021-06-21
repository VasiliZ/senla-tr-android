package com.github.rtyvz.senla.tr.myapplication.task

import android.os.AsyncTask
import com.github.rtyvz.senla.tr.myapplication.ListManager

class SleepingAsyncTask(
    private val objectToWakeUp: Object,
    private val listManager: ListManager
) :
    AsyncTask<Void, Void, Void>() {

    companion object {
        private const val YUP = "Yup!"
    }

    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            synchronized(objectToWakeUp) {
                objectToWakeUp.wait()
                listManager.setData(YUP)
            }
        }
    }
}