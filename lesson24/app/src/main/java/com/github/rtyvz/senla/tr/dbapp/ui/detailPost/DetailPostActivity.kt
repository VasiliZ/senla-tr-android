package com.github.rtyvz.senla.tr.dbapp.ui.detailPost

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.rtyvz.senla.tr.dbapp.databinding.DetailPostActivityBinding
import com.github.rtyvz.senla.tr.dbapp.models.DetailPost
import com.github.rtyvz.senla.tr.dbapp.provider.TaskProvider
import com.github.rtyvz.senla.tr.dbapp.ui.comments.CommentsActivity

class DetailPostActivity : AppCompatActivity() {
    private lateinit var binding: DetailPostActivityBinding
    private lateinit var gettingDataFaultReceiver: BroadcastReceiver
    private lateinit var detailPostReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private var detailPost: DetailPost? = null

    companion object {
        const val EXTRA_POST_ID = "POST_ID"
        const val BROADCAST_GETTING_DETAIL_POST = "local:BROADCAST_GETTING_DETAIL_POST"
        const val BROADCAST_GETTING_DETAIL_POST_FAULTED = "local:BROADCAST_GET_DETAIL_POST_FAULTED"
        const val EXTRA_GETTING_DETAIL_POST = "GETTING_DETAIL_POST"
        const val EXTRA_GETTING_DETAIL_POST_FAULTED = "GETTING_DETAIL_POST_FAULTED"
        private const val POST_ID_DEFAULT_VALUE = 0L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DetailPostActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        TaskProvider.provideDetailPost(intent.getLongExtra(EXTRA_POST_ID, POST_ID_DEFAULT_VALUE))
        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        initGettingDataFaultReceiver()
        initDetailPostReceiver()

        binding.commentsButton.setOnClickListener {
            startActivity(Intent(this, CommentsActivity::class.java).apply {
                putExtra(CommentsActivity.EXTRA_POST_ID, detailPost?.postId)
            })
        }
    }

    override fun onResume() {
        super.onResume()

        registerIsFaultedGettingDetailPost()
        registerGettingDetailPost()
    }

    private fun registerGettingDetailPost() {
        localBroadcastManager.registerReceiver(
            detailPostReceiver, IntentFilter(BROADCAST_GETTING_DETAIL_POST)
        )
    }

    private fun registerIsFaultedGettingDetailPost() {
        localBroadcastManager.registerReceiver(
            gettingDataFaultReceiver, IntentFilter(BROADCAST_GETTING_DETAIL_POST_FAULTED)
        )
    }

    private fun initDetailPostReceiver() {
        detailPostReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent != null) {
                    detailPost =
                        intent.getParcelableExtra(EXTRA_GETTING_DETAIL_POST)
                    binding.apply {
                        fullNameAuthorTextView.text = detailPost?.authorFullName
                        postTitleTextView.text = detailPost?.postTitle
                        userEmailTextView.text = detailPost?.email
                        postBodyTextView.text = detailPost?.postBody
                    }
                }
            }
        }
    }

    private fun initGettingDataFaultReceiver() {
        gettingDataFaultReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(
                    this@DetailPostActivity, intent?.getStringExtra(
                        EXTRA_GETTING_DETAIL_POST_FAULTED
                    ), Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(detailPostReceiver)
        localBroadcastManager.unregisterReceiver(gettingDataFaultReceiver)

        super.onPause()
    }
}