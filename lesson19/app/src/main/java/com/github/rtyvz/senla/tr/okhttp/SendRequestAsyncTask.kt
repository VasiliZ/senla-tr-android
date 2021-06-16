package com.github.rtyvz.senla.tr.okhttp

import android.os.AsyncTask
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class SendRequestAsyncTask(
    private val url: String,
    private val callbacks: TaskCallbacks
) :
    AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        Thread.sleep(2000)
        val request = Request.Builder().url(URL(url)).build()
        OkHttpClient()
            .newCall(request).execute().use {
                return it.body?.string()
            }
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        result?.let {
            callbacks.onPostExecute(it)
        }
    }
}