package com.github.rtyvz.senla.tr.dbapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.dbapp.databinding.PostListActivityBinding
import com.github.rtyvz.senla.tr.dbapp.provider.TaskProvider

class PostActivity : AppCompatActivity() {
    private lateinit var binding: PostListActivityBinding
    private val postAdapter by lazy {
        PostAdapter()
    }
    private lateinit var errorFetchPostReceiver: BroadcastReceiver
    private lateinit var postDataReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager

    companion object {
        const val BROADCAST_FAULT_FETCH_POST = "local:BROADCAST_FAULT_FETCH_POST"
        const val BROADCAST_POST_DATA = "local:BROADCAST_POST_DATA"
        const val EXTRA_FAULT_FETCH_POST = "FAULT_FETCH_POST"
        const val EXTRA_POST_DATA = "FAULT_POST_DATA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PostListActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        TaskProvider.providePostWithEmail()
        initRecyclerView()
        initFaultFetchingPostReceiver()
        initPostDataReceiver()
    }

    private fun initPostDataReceiver() {
        postDataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                postAdapter.setData(
                    intent?.getParcelableArrayListExtra(
                        EXTRA_POST_DATA
                    ) ?: emptyList()
                )
            }
        }
    }

    private fun initFaultFetchingPostReceiver() {
        errorFetchPostReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                binding.apply {
                    errorEditText.isVisible = true
                    postListRecycler.isVisible = false
                    errorEditText.text = intent?.getStringExtra(EXTRA_FAULT_FETCH_POST)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

        registerFaultFetchingPostReceiver()
        registerPostDataReceiver()
    }

    private fun registerFaultFetchingPostReceiver() {
        localBroadcastManager.registerReceiver(
            errorFetchPostReceiver, IntentFilter(
                BROADCAST_FAULT_FETCH_POST
            )
        )
    }

    private fun registerPostDataReceiver() {
        localBroadcastManager.registerReceiver(postDataReceiver, IntentFilter(BROADCAST_POST_DATA))
    }

    private fun initRecyclerView() {
        binding.postListRecycler.apply {
            adapter = postAdapter
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(postDataReceiver)
        localBroadcastManager.unregisterReceiver(errorFetchPostReceiver)

        super.onPause()
    }
}