package com.github.rtyvz.senla.tr.okhttp

import android.content.Intent
import android.os.AsyncTask
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class SendRequestAsyncTask(
    private val url: String,
    private val sendValue: String
) : AsyncTask<Void, Void, String>() {

    companion object {
        private const val EMPTY_STRING = ""
    }

    override fun doInBackground(vararg params: Void?): String? {
        val request =
            Request.Builder()
                .url(
                    URL(StringBuilder(url).append(sendValue).toString())
                ).build()
        OkHttpClient()
            .newCall(request).execute().use {
                return it.body?.string()
            }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        LocalBroadcastManager.getInstance(App.INSTANCE)
            .sendBroadcastSync(Intent(MainActivity.BROADCAST_RESPONSE_VALUE).apply {
                putExtra(MainActivity.EXTRA_REQUEST, result ?: EMPTY_STRING)
            })
    }
}
