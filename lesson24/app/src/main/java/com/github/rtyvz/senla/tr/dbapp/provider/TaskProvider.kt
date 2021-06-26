package com.github.rtyvz.senla.tr.dbapp.provider

import com.github.rtyvz.senla.tr.dbapp.task.ProvidePostWithEmail

object TaskProvider {
    fun providePostWithEmail() = ProvidePostWithEmail().getPostWithEmail(DbProvider.provideDb())
}