package com.github.rtyvz.senla.tr.okhttp

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment

class HandleTaskFragment : Fragment() {
    private var callBacks: TaskCallbacks? = null
    private var task: SendRequestAsyncTask? = null
    private val regexEscapeParam = "([\\\\\\/\\^\\&])".toRegex()

    companion object {
        const val TAG = "HandleTaskFragment"
        private const val URL_WITHOUT_PARAM =
            "https://pub.zame-dev.org/senla-training-addition/lesson-19.php?param="
        private const val EMPTY_STRING = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        activity?.let {
            task = SendRequestAsyncTask(
                StringBuilder(URL_WITHOUT_PARAM)
                    .append(
                        (it as MainActivity).binding?.inputValueTextEdit?.text?.replace(
                            regexEscapeParam
                        ) {
                            EMPTY_STRING
                        })
                    .toString(), it
            ).execute() as SendRequestAsyncTask
        }

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