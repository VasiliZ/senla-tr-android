package com.github.rtyvz.senla.tr.dbapp.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.dbapp.App
import com.github.rtyvz.senla.tr.dbapp.models.CommentWithEmailEntity
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider
import com.github.rtyvz.senla.tr.dbapp.ui.comments.CommentsActivity

class ProvidePostCommentsTask {
    fun getPostComments(postId: Long) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
        Task.callInBackground {
            DbProvider.provideDb().getPostComments(postId)
        }.continueWith(Continuation<List<CommentWithEmailEntity>, Task<Unit>> {
            if (it.isFaulted) {
                localBroadcastManager
                    .sendBroadcastSync(Intent(CommentsActivity.BROADCAST_FAULT_GETTING_COMMENTS_TASK))
                return@Continuation null
            } else {
                localBroadcastManager
                    .sendBroadcastSync(Intent(CommentsActivity.BROADCAST_GETTING_COMMENTS_TASK_DATA)
                        .apply {
                            putExtra(
                                CommentsActivity.EXTRA_GETTING_COMMENT_TASK_DATA,
                                ArrayList(it.result)
                            )
                        })
                return@Continuation null
            }
        }, Task.UI_THREAD_EXECUTOR)
    }
}