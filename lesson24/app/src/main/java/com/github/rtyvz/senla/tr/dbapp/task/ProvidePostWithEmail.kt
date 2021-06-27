package com.github.rtyvz.senla.tr.dbapp.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.dbapp.App
import com.github.rtyvz.senla.tr.dbapp.R
import com.github.rtyvz.senla.tr.dbapp.models.PostAndUserEmailEntity
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider
import com.github.rtyvz.senla.tr.dbapp.ui.post.PostActivity

class ProvidePostWithEmail {
    fun getPostWithEmail() {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
        Task.callInBackground {
            DbProvider.provideDb().getPostWithEmails()
        }.continueWith(Continuation<List<PostAndUserEmailEntity>, Unit> {
            if (it.isFaulted) {
                localBroadcastManager
                    .sendBroadcastSync(Intent(PostActivity.BROADCAST_FAULT_FETCH_POST).apply {
                        putExtra(
                            PostActivity.EXTRA_FAULT_FETCH_POST,
                            App.INSTANCE.getString(R.string.fetching_data_faulted)
                        )
                    })
                return@Continuation null
            } else {
                localBroadcastManager
                    .sendBroadcastSync(Intent(PostActivity.BROADCAST_POST_DATA).apply {
                        putParcelableArrayListExtra(
                            PostActivity.EXTRA_POST_DATA,
                            ArrayList(it.result)
                        )
                    })
                return@Continuation null
            }
        }, Task.UI_THREAD_EXECUTOR)
    }
}