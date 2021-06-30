package com.github.rtyvz.senla.tr.dbapp.task

import bolts.Task
import com.github.rtyvz.senla.tr.dbapp.db.helper.DbHelper
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider

class ProvideChangeRateCommentTask {

    fun changeCommentRate(value: String, commentId: Long, postId: Long) {
        Task.callInBackground {
            DbHelper()
                .changeCommentRate(value, commentId, DbProvider.provideDb().writableDatabase)
        }.onSuccess {
            return@onSuccess ProvidePostCommentsTask().getPostComments(postId)
        }.continueWith {
            return@continueWith null
        }
    }
}