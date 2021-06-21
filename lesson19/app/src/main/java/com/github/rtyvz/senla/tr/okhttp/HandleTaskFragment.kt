package com.github.rtyvz.senla.tr.okhttp

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class HandleTaskFragment : Fragment() {
    private var callBacks: TaskCallbacks? = null
    private var task: SendRequestAsyncTask? = null
    private val regexEscapeParam = "([\\\\/<>&])".toRegex()
    private lateinit var inputValue: String

    companion object {
        const val TAG = "HandleTaskFragment"
        private const val URL_WITHOUT_PARAM =
            "https://pub.zame-dev.org/senla-training-addition/lesson-19.php?param="
        private const val EMPTY_STRING = ""
        const val EXTRA_INPUT_VALUE = "INPUT_VALUE"

        class SendRequestAsyncTask(
            private val url: String,
            private val callbacks: TaskCallbacks?
        ) :
            AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {
                if (isCancelled) Thread.interrupted()

                val request = Request.Builder().url(URL(url)).build()
                OkHttpClient()
                    .newCall(request).execute().use {
                        return it.body?.string()
                    }
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                result?.let {
                    callbacks?.onPostExecute(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        arguments?.let {
            inputValue = it.getString(EXTRA_INPUT_VALUE, "")
        }

        callBacks?.let {
            startTask()
        }
    }

    fun restartTask(newData: String) {
        task?.cancel(true)
        inputValue = newData
        startTask()
    }

    private fun startTask() {
        task = SendRequestAsyncTask(
            StringBuilder(URL_WITHOUT_PARAM)
                .append(inputValue.replace(regexEscapeParam) {
                    EMPTY_STRING
                })
                .toString(), callBacks
        ).execute() as SendRequestAsyncTask
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callBacks = (activity as MainActivity)

        if (task?.status == AsyncTask.Status.RUNNING) {
            (activity as MainActivity).taskRunningYet()
        }
    }

    override fun onDetach() {
        callBacks = null

        super.onDetach()
    }
}

interface TaskCallbacks {
    fun taskRunningYet()
    fun onPostExecute(it: String)
}