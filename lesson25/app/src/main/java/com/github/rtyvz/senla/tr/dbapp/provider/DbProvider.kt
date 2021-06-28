package com.github.rtyvz.senla.tr.dbapp.provider

import com.github.rtyvz.senla.tr.dbapp.App
import com.github.rtyvz.senla.tr.dbapp.db.AppDb

object DbProvider {
    fun provideDb(): AppDb = AppDb(App.INSTANCE)
}