package com.github.rtyvz.senla.tr.dbapp.provider

import com.github.rtyvz.senla.tr.dbapp.task.ProvideDetailPostTask
import com.github.rtyvz.senla.tr.dbapp.task.ProvidePostCommentsTask
import com.github.rtyvz.senla.tr.dbapp.task.ProvidePostWithEmail

object TaskProvider {
    fun providePostWithEmail() = ProvidePostWithEmail().getPostWithEmail()
    fun provideDetailPost(postId: Long) =
        ProvideDetailPostTask().getDetailPostData(postId)

    fun provideComments(postId: Long) = ProvidePostCommentsTask().getPostComments(postId)
}