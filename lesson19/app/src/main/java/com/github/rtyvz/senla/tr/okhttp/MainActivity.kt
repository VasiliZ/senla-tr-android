package com.github.rtyvz.senla.tr.okhttp

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.okhttp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progress: ProgressDialog
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private lateinit var requestReceiver: BroadcastReceiver

    companion object {
        const val EXTRA_REQUEST = "REQUEST"
        const val BROADCAST_RESPONSE_VALUE = "local:BROADCAST_RESPONSE_VALUE"
        private const val EXTRA_TEXT_EDIT_VALUE: String = "TEXT_EDIT_VALUE"
        private const val EXTRA_RESULT_VALUE: String = "RESULT_VALUE"
        private const val EMPTY_STRING: String = ""
        private const val URL_WITHOUT_PARAM =
            "https://pub.zame-dev.org/senla-training-addition/lesson-19.php?param="
        private var task: SendRequestAsyncTask? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (App.INSTANCE.state == null) {
            App.INSTANCE.state = State()
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        initStateValue()
        initProgressDialog()
        initRequestReceiver()

        binding.apply {
            sendButton.setOnClickListener {
                if (!progress.isShowing) {
                    progress.show()
                }
                task = SendRequestAsyncTask(
                    URL_WITHOUT_PARAM,
                    binding.inputValueTextEdit.text.toString()
                )
                task?.execute()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (task?.status == AsyncTask.Status.RUNNING) {
            progress.show()
        }

        registerRequestReceiver()
    }

    private fun initStateValue() {
        binding.apply {
            inputValueTextEdit.setText(App.INSTANCE.state?.inputValue)
            responseTextView.text = App.INSTANCE.state?.responseValue
        }
    }

    private fun initRequestReceiver() {
        requestReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val response = intent?.extras?.getString(EXTRA_REQUEST) ?: EMPTY_STRING
                binding.responseTextView.text = response
                App.INSTANCE.state?.responseValue = response
                progress.dismiss()
            }
        }
    }

    private fun registerRequestReceiver(){
        localBroadcastManager.registerReceiver(
            requestReceiver, IntentFilter(
                BROADCAST_RESPONSE_VALUE
            )
        )
    }

    private fun initProgressDialog() {
        progress = ProgressDialog(this@MainActivity)
        progress.setMessage(getString(R.string.wait))
        progress.setCanceledOnTouchOutside(false)
        progress.isIndeterminate = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TEXT_EDIT_VALUE, binding.inputValueTextEdit.text.toString())
        outState.putString(EXTRA_RESULT_VALUE, binding.responseTextView.text.toString())
    }

    override fun onPause() {
        progress.dismiss()
        localBroadcastManager.unregisterReceiver(requestReceiver)
        App.INSTANCE.state?.inputValue = binding.inputValueTextEdit.text.toString()

        super.onPause()
    }
}