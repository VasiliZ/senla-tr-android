package com.github.rtyvz.senla.tr.okhttp

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class SendRequestAsyncTask(
    private val url: String,
    private val localBroadcastManager: LocalBroadcastManager,
    private val sendValue: String
) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void?): String? {
        val request =
            Request.Builder().url(URL(StringBuilder(url).append(sendValue).toString())).build()
        OkHttpClient()
            .newCall(request).execute().use {
                return it.body?.string()
            }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        result?.let {
            localBroadcastManager.sendBroadcast(Intent(MainActivity.BROADCAST_RESPONSE_VALUE).apply {
                putExtra(MainActivity.EXTRA_REQUEST, it)
            })
        }
    }
}
