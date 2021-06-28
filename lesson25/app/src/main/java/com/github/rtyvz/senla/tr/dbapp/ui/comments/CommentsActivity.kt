package com.github.rtyvz.senla.tr.dbapp.ui.comments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.dbapp.R
import com.github.rtyvz.senla.tr.dbapp.databinding.CommentsActivityBinding
import com.github.rtyvz.senla.tr.dbapp.provider.TaskProvider

class CommentsActivity : AppCompatActivity() {
    private lateinit var binding: CommentsActivityBinding
    private lateinit var faultFetchingDataReceiver: BroadcastReceiver
    private lateinit var successFetchingDataReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private val commentsAdapter by lazy {
        CommentsAdapter()
    }

    companion object {
        const val EXTRA_POST_ID = "POST_ID"
        const val BROADCAST_FAULT_GETTING_COMMENTS_TASK =
            "local:BROADCAST_FAULT_GETTING_COMMENTS_TASK"
        const val BROADCAST_GETTING_COMMENTS_TASK_DATA =
            "local:BROADCAST_GETTING_COMMENTS_TASK_DATA"
        const val EXTRA_GETTING_COMMENT_TASK_DATA = "GETTING_COMMENT_TASK_DATA"
        private const val DEFAULT_POST_ID_VALUE = 0L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = CommentsActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        initFaultFetchingDataReceiver()
        initSuccessFetchingDataReceiver()
        TaskProvider.provideComments(intent.getLongExtra(EXTRA_POST_ID, DEFAULT_POST_ID_VALUE))
        binding.commentsRecycler.adapter = commentsAdapter
    }

    override fun onResume() {
        super.onResume()

        registerFaultReceiver()
        registerSuccessReceiver()
    }

    private fun registerSuccessReceiver() {
        localBroadcastManager.registerReceiver(
            successFetchingDataReceiver, IntentFilter(
                BROADCAST_GETTING_COMMENTS_TASK_DATA
            )
        )
    }

    private fun registerFaultReceiver() {
        localBroadcastManager.registerReceiver(
            faultFetchingDataReceiver, IntentFilter(
                BROADCAST_FAULT_GETTING_COMMENTS_TASK
            )
        )
    }

    private fun initSuccessFetchingDataReceiver() {
        successFetchingDataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                commentsAdapter.setData(
                    intent?.getParcelableArrayListExtra(
                        EXTRA_GETTING_COMMENT_TASK_DATA
                    ) ?: emptyList()
                )
            }
        }
    }

    private fun initFaultFetchingDataReceiver() {
        faultFetchingDataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(
                    this@CommentsActivity,
                    getString(R.string.fetching_data_faulted),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(faultFetchingDataReceiver)
        localBroadcastManager.unregisterReceiver(successFetchingDataReceiver)

        super.onPause()
    }
}