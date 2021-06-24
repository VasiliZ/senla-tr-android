package com.github.rtyvz.senla.tr.myapplication.task

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.MainActivity

class ReadDataAsyncTask(
    private val listData: MutableList<String>
) : AsyncTask<Void, List<String>?, List<String>?>() {

    companion object {
        private const val TIME_THREAD_SLEEP = 100L
    }

    override fun doInBackground(vararg params: Void?): List<String>? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            Thread.sleep(TIME_THREAD_SLEEP)
            val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
            localBroadcastManager
                .sendBroadcastSync(Intent(MainActivity.BROADCAST_READ_DATA).apply {
                    putStringArrayListExtra(MainActivity.EXTRA_READ_DATA, ArrayList(listData))
                })
        }
    }
}