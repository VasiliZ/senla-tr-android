package com.github.rtyvz.senla.tr.myapplication.task

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.myapplication.App
import com.github.rtyvz.senla.tr.myapplication.MainActivity

class SleepingAsyncTask(
    private val objectToWakeUp: Object
) : AsyncTask<Void, Void, Void>() {

    companion object {
        private const val YUP = "Yup!"
    }

    override fun doInBackground(vararg params: Void?): Void? {
        while (true) {
            if (isCancelled) Thread.interrupted()
            synchronized(objectToWakeUp) {
                objectToWakeUp.wait()
                val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
                localBroadcastManager
                    .sendBroadcastSync(Intent(MainActivity.BROADCAST_SEND_YUP).apply {
                        putExtra(MainActivity.EXTRA_YUP, YUP)
                    })
            }
        }
    }
}