package com.github.rtyvz.senla.tr.dbapp.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.dbapp.App
import com.github.rtyvz.senla.tr.dbapp.R
import com.github.rtyvz.senla.tr.dbapp.models.DetailPost
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider
import com.github.rtyvz.senla.tr.dbapp.ui.detailPost.DetailPostActivity

class ProvideDetailPostTask {
    fun getDetailPostData(postId: Long) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
        Task.callInBackground {
            DbProvider.provideDb().getDetailPost(postId)
        }.continueWith(Continuation<DetailPost?, Task<Unit>> {
            if (it.isFaulted) {
                localBroadcastManager
                    .sendBroadcastSync(Intent(DetailPostActivity.BROADCAST_GETTING_DETAIL_POST_FAULTED)
                        .apply {
                            putExtra(
                                DetailPostActivity.EXTRA_GETTING_DETAIL_POST_FAULTED,
                                App.INSTANCE.getString(
                                    R.string.fetching_data_faulted
                                )
                            )
                        })
                return@Continuation null
            } else {
                localBroadcastManager
                    .sendBroadcastSync(Intent(DetailPostActivity.BROADCAST_GETTING_DETAIL_POST)
                        .apply {
                            putExtra(
                                DetailPostActivity.EXTRA_GETTING_DETAIL_POST,
                                it.result
                            )
                        })
                return@Continuation null
            }
        }, Task.UI_THREAD_EXECUTOR)
    }
}